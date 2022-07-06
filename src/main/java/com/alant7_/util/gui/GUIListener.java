package com.alant7_.util.gui;

import com.alant7_.util.event.PlayerGuiCloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.HashMap;
import java.util.Map;

public class GUIListener implements Listener {

    private final Map<Player, InventoryCloseEvent> activeCloseEvents = new HashMap<>();

    private final GUIManager guiManager;

    public GUIListener(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        AbstractGUI gui = guiManager.getOpenInventory(event.getWhoClicked().getUniqueId());
        if (gui == null) {
            return;
        }

        try {
            gui.interact(event);
            if (gui.isCancelled()) {
                event.setCancelled(true);
                gui.setCancelled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {

        AbstractGUI gui = guiManager.getOpenInventory(event.getPlayer().getUniqueId());
        if (gui == null) return;

        activeCloseEvents.put((Player) event.getPlayer(), event);

        guiManager.removeInventory(event.getPlayer().getUniqueId());

        PlayerGuiCloseEvent event1 = new PlayerGuiCloseEvent((Player) event.getPlayer(), gui);

        Bukkit.getPluginManager().callEvent(event1);

        gui.close(CloseInitiator.BUKKIT);

        gui.dispose();

    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onInventoryClose2(InventoryCloseEvent event) {

        activeCloseEvents.remove((Player) event.getPlayer());

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getView().getPlayer();
        AbstractGUI gui = guiManager.openGuis.get(player.getUniqueId());

        if (gui != null && !gui.isDraggingEnabled()) {
            event.setCancelled(true);
        }
    }

    public boolean isInEvent(Player player) {
        return activeCloseEvents.containsKey(player);
    }

}
