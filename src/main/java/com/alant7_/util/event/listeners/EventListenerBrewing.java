package com.alant7_.util.event.listeners;

import com.alant7_.util.BukkitPlugin;
import com.alant7_.util.event.PlayerBrewPotionEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import static com.alant7_.util.event.PlayerBrewPotionEvent.isPotion;

import java.util.HashMap;

public class EventListenerBrewing implements Listener {

    private HashMap<Location, PlayerBrewPotionEvent.ActiveBrewing> activeBrewings = new HashMap<>();

    private BukkitPlugin plugin;

    public EventListenerBrewing(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getInventory().getType() != InventoryType.BREWING)
            return;

        BrewerInventory inv = (BrewerInventory) event.getInventory();

        BrewingStand stand = inv.getHolder();

        Player player = (Player) event.getWhoClicked();

        PlayerBrewPotionEvent.ActiveBrewing brewing = activeBrewings.getOrDefault(stand.getLocation(), null);
        if (brewing == null) {
            brewing = new PlayerBrewPotionEvent.ActiveBrewing();
            activeBrewings.put(stand.getLocation(), brewing);
        }

        if (event.isShiftClick()) {
            if (event.getInventory().equals(event.getView().getTopInventory())) {

                ItemStack clicked = event.getCurrentItem();

                if (isPotion(clicked)) {
                    if (event.getSlotType() == InventoryType.SlotType.CRAFTING) {
                        brewing.remove(event.getRawSlot());
                    } else {
                        for (int i = 0; i < 3; i++) {
                            if (inv.getContents()[i] == null || inv.getContents()[i].getType() == Material.AIR) {
                                brewing.add(i, clicked, player);
                                break;
                            }
                        }
                    }
                }

            }

            return;

        }

        switch (event.getAction()) {
            case PLACE_ALL:
            case PLACE_SOME:
            case PLACE_ONE:
                if (event.getRawSlot() > 4) {
                    return;
                }
                if (isPotion(event.getCursor()))
                    brewing.add(event.getSlot(), event.getCursor(), player);
                break;
            case PICKUP_ALL:
            case PICKUP_ONE:
            case PICKUP_SOME:
            case DROP_ALL_SLOT:
            case DROP_ONE_SLOT:
                if (event.getRawSlot() > 4) {
                    return;
                }
                brewing.remove(event.getSlot());
                break;
            case SWAP_WITH_CURSOR:
                if (event.getRawSlot() > 4) {
                    return;
                }
                if (isPotion(event.getCursor())) {
                    brewing.remove(event.getRawSlot());
                    brewing.add(event.getRawSlot(), event.getCursor(), player);
                }
                break;
            case MOVE_TO_OTHER_INVENTORY:
                break;
            case HOTBAR_SWAP:
            case HOTBAR_MOVE_AND_READD:
                if (event.getRawSlot() > 4) {
                    return;
                }

                ItemStack replacement = event.getView().getBottomInventory().getItem(event.getHotbarButton());
                brewing.remove(event.getRawSlot());
                if (isPotion(replacement)) {
                    brewing.add(event.getRawSlot(), replacement, player);
                }
                break;
        }

    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemDrag(InventoryDragEvent event) {

        if (event.getInventory().getType() != InventoryType.BREWING)
            return;

        BrewerInventory inv = (BrewerInventory) event.getInventory();
        BrewingStand stand = inv.getHolder();

        PlayerBrewPotionEvent.ActiveBrewing brewing = activeBrewings.getOrDefault(stand.getLocation(), null);
        if (brewing == null) {
            brewing = new PlayerBrewPotionEvent.ActiveBrewing();
            activeBrewings.put(stand.getLocation(), brewing);
        }

        PlayerBrewPotionEvent.ActiveBrewing finalBrewing = brewing;
        event.getNewItems().forEach((slot, stack) -> {
            if (slot > 4) {
                return;
            }

            finalBrewing.add(slot, stack, (Player) event.getWhoClicked());
        });

    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPotionBrew(BrewEvent event) {

        PlayerBrewPotionEvent.ActiveBrewing brewing = activeBrewings.getOrDefault(event.getBlock().getLocation(), null);
        if (brewing == null) {
            return;
        }

        activeBrewings.remove(event.getBlock().getLocation());

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (int i = 0; i < 3; i++) {
                brewing.setResult(i, event.getContents().getItem(i));
            }

            PlayerBrewPotionEvent.BrewingTable[] tables = brewing.getAsTables();
            for (int i = 0; i < 3; i++) {
                if (tables[i].getIngredient() == null || tables[i].getResult() == null) {
                    continue;
                }

                PlayerBrewPotionEvent.BrewingTable table = tables[i];
                PlayerBrewPotionEvent event1 = new PlayerBrewPotionEvent(
                        table.getBrewer(),
                        event.getContents().getHolder(),
                        brewing.getIngredient(),
                        table.getIngredient(),
                        table.getResult()
                );

                Bukkit.getPluginManager().callEvent(event1);
            }
        }, 1);
    }

}
