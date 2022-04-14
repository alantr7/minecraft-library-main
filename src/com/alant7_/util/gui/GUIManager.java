package com.alant7_.util.gui;

import com.alant7_.util.AlanJavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GUIManager {

    final HashMap<UUID, AbstractGUI> openGuis = new HashMap<>();

    final GUIListener guiListener;

    public GUIManager() {
        this.guiListener = new GUIListener(this);
    }

    public AbstractGUI getOpenInventory(Player player) {
        return getOpenInventory(player.getUniqueId());
    }

    public AbstractGUI getOpenInventory(UUID uuid) {
        return openGuis.get(uuid);
    }

    public void disposeInventory(Player player) {
        disposeInventory(player.getUniqueId());
    }

    public void disposeInventory(UUID uuid) {
        openGuis.remove(uuid).dispose();
    }

    void removeInventory(UUID uuid) {
        openGuis.remove(uuid);
    }

    public List<Player> getPlayersWithGUI(Class<? extends AbstractGUI> guis) {

        List<Player> players = new ArrayList<>();

        openGuis.forEach((uuid, gui) -> {
            if (gui.getClass() == guis) {

                Player player = Bukkit.getPlayer(uuid);
                if (player != null)
                    players.add(player);

            }
        });

        return players;

    }

    public GUIListener getEventListener() {
        return guiListener;
    }

    public void close() {
        for (UUID uuid : openGuis.keySet()) {
            AbstractGUI gui = openGuis.get(uuid);
            gui.close();
        }

        openGuis.clear();
    }

}
