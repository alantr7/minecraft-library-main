package com.alant7_.util.config;

import com.alant7_.util.Formatter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ConfigurationItem {

    private final Map<String, Object> properties;

    private final Material material;

    private final String displayName;

    private final boolean hasEnchantedEffect;

    private final List<String> lore;

    private final ConfigurationSection section;

    public ConfigurationItem(@NotNull ConfigurationSection section) {

        this.section = section;

        material = Material.valueOf(section.getString("ITEM").toUpperCase());

        displayName = Formatter.formatColors("§f" + section.getString("NAME"));

        hasEnchantedEffect = section.getBoolean("ENCHANTED_EFFECT");

        lore = section.getStringList("LORE");

        lore.replaceAll(line -> Formatter.formatColors("&f" + line));

        properties = section.getValues(false);

    }

    public List<String> getLoreModified(UnaryOperator<String> op) {

        List<String> copy = new ArrayList<>(lore);

        copy.replaceAll(op);

        return copy;

    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean hasEnchantedEffect() {
        return hasEnchantedEffect;
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Object getPropertyOrDefault(String key, Object defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

    public ItemStack toItemStack() {
        return toItemStack(hasDisplayName() ? getDisplayName() : "");
    }

    public ItemStack toItemStack(String displayName) {
        return toItemStack(displayName, getMaterial());
    }

    public ItemStack toItemStack(String displayName, Material material) {
        return toItemStack(displayName, material, getLore());
    }

    public ItemStack toItemStack(String displayName, Material material, List<String> lore) {

        ItemStack stack = new ItemStack(material);

        ItemMeta meta = stack.getItemMeta();

        assert meta != null;

        meta.setDisplayName(displayName);

        meta.setLore(lore);

        stack.setItemMeta(meta);

        return stack;

    }

}
