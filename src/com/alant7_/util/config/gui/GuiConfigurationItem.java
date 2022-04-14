package com.alant7_.util.config.gui;

import com.alant7_.util.config.gui.condition.GuiConfigurationItemCondition;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiConfigurationItem {

    private final List<Integer> slots;

    private final GuiConfigurationItemProperties defaultProperties;

    private final Map<String, GuiConfigurationItemProperties> conditionalProperties;

    public GuiConfigurationItem(List<Integer> slots, GuiConfigurationItemProperties defaultProperties, Map<String, GuiConfigurationItemProperties> conditionalProperties) {
        this.slots = slots;
        this.defaultProperties = defaultProperties;
        this.conditionalProperties = conditionalProperties;
    }

    public ItemStack toItemStack(Player player) {

        return defaultProperties.toItemStack(player);

    }

    public ItemStack toItemStack(Player player, String condition) {

        GuiConfigurationItemProperties conditionalProps = this.conditionalProperties.get(condition.toLowerCase());

        return (conditionalProps != null ? defaultProperties.combine(conditionalProps) : defaultProperties).toItemStack(player);

    }

    public List<Integer> getSlots() {
        return slots;
    }

}
