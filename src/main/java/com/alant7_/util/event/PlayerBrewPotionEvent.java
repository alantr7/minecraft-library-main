package com.alant7_.util.event;

import com.alant7_.util.reflections.Pair;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PlayerBrewPotionEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private Player player;

    private BrewingStand brewingStand;

    private ItemStack originalPotion;

    private ItemStack resultPotion;

    private ItemStack ingredient;

    private PotionType ingredientType;

    private PotionType resultType;

    public PlayerBrewPotionEvent(Player player, BrewingStand brewingStand, ItemStack ingredient, ItemStack originalPotion, ItemStack resultPotion) {
        this.player = player;
        this.brewingStand = brewingStand;
        this.originalPotion = originalPotion;
        this.resultPotion = resultPotion;
        this.ingredient = ingredient;
        this.ingredientType = ingredient != null ? ((PotionMeta) ingredient.getItemMeta()).getBasePotionData().getType() : null;
        this.resultType = resultPotion != null ? ((PotionMeta) resultPotion.getItemMeta()).getBasePotionData().getType() : null;
    }

    public ItemStack getIngredient() {
        return ingredient;
    }

    public BrewingStand getBrewingStand() {
        return brewingStand;
    }

    public ItemStack getResultPotion() {
        return resultPotion;
    }

    public ItemStack getOriginalPotion() {
        return originalPotion;
    }

    public PotionType getOriginalPotionType() {
        return ingredientType;
    }

    public PotionType getResultPotionType() {
        return resultType;
    }

    public Player getBrewer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public static class ActiveBrewing {

        private ItemStack ingredient;

        private ItemStack[] contents = new ItemStack[3];
        private ItemStack[] results = new ItemStack[3];
        private Player[] brewers = new Player[3];

        public void add(int slot, ItemStack stack, Player brewer) {
            if (stack == null || stack.getType() == Material.AIR) {
                remove(slot);
                return;
            }

            contents[slot] = stack.clone();
            brewers[slot] = brewer;
        }

        public void remove(int slot) {
            if (slot > 2 || slot < 0)
                return;
            contents[slot] = null;
            brewers[slot] = null;
            results[slot] = null;
        }

        public void setIngredient(ItemStack stack) {
            ingredient = stack;
        }

        public void setResult(int slot, ItemStack result) {
            results[slot] = result;
        }

        public ItemStack getIngredient() {
            return ingredient;
        }

        public ItemStack[] getContents() {
            return contents;
        }

        public ItemStack[] getResults() {
            return results;
        }

        public Player[] getBrewers() {
            return brewers;
        }

        public HashMap<ItemStack, Player> getContentsAsMap() {
            HashMap<ItemStack, Player> map = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                if (contents[i] != null) {
                    map.put(contents[i], brewers[i]);
                }
            }

            return map;
        }

        public HashMap<ItemStack, Player> getResultsAsMap() {
            HashMap<ItemStack, Player> map = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                if (results[i] != null) {
                    map.put(results[i], brewers[i]);
                }
            }

            return map;
        }

        public BrewingTable[] getAsTables() {
            BrewingTable[] tables = new BrewingTable[3];
            for (int i = 0; i < 3; i++) {
                tables[i] = new BrewingTable(contents[i], results[i], brewers[i]);
            }

            return tables;
        }

    }

    public static class BrewingTable {

        private ItemStack ingredient;

        private ItemStack result;

        private Player brewer;

        public BrewingTable(ItemStack stack, ItemStack result, Player brewer) {
            this.ingredient = stack;
            this.result = result;
            this.brewer = brewer;
        }

        public ItemStack getIngredient() {
            return ingredient;
        }

        public ItemStack getResult() {
            return result;
        }

        public Player getBrewer() {
            return brewer;
        }
    }

    public static boolean isPotion(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Material material = stack.getType();
        return material == Material.POTION || material == Material.SPLASH_POTION || material == Material.LINGERING_POTION;
    }

}
