package com.alant7_.util.config.gui;

import com.alant7_.util.config.gui.condition.GuiConfigurationItemCondition;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiConfigurationItemProperties {

    private Material material;

    private int amount;

    private List<String> lore;

    private final List<GuiConfigurationItemCondition> conditions = new ArrayList<>();

    public boolean conditionsSatisfied(Player player) {

        for (GuiConfigurationItemCondition condition : conditions) {
            if (!condition.isSatisfied(player))
                return false;
        }

        return true;

    }

    public GuiConfigurationItemProperties combine(GuiConfigurationItemProperties props) {

        if (props == null)

            return this;

        Material material = props.material != null ? props.material : this.material;

        int amount = props.amount != -1 ? props.amount : this.amount;

        List<String> lore = props.lore != null ? props.lore : this.lore;

        GuiConfigurationItemProperties result = new GuiConfigurationItemProperties();

        result.material = material;
        result.amount = amount;
        result.lore = lore;

        return result;

    }

    public ItemStack toItemStack(Player player) {

        ItemStack stack = new ItemStack(material);

        stack.setAmount(Math.max(amount, 1));

        ItemMeta meta = stack.getItemMeta();

        assert meta != null;

        if (lore != null) {
            meta.setLore(lore);
        }

        stack.setItemMeta(meta);

        return stack;

    }

}
