package com.alant7_.util.items;

import com.alant7_.util.Formatter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack stack;

    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this.stack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack stack) {
        this(stack, true);
    }

    public ItemBuilder(ItemStack stack, boolean clone) {
        this.stack = clone ? stack.clone() : stack;
    }

    public ItemBuilder name(String name) {
        acquireMeta();
        meta.setDisplayName(name);
        setMeta();

        return this;
    }

    public ItemBuilder material(Material material) {
        stack.setType(material);
        refreshMeta();

        return this;
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        acquireMeta();
        meta.setLore(lore);
        setMeta();

        return this;
    }

    public ItemBuilder lore(String... lines) {
        lore(Arrays.asList(lines));
        return this;
    }

    public ItemBuilder lore(Consumer<List<String>> lore) {
        acquireMeta();
        if (!meta.hasLore())
            meta.setLore(Collections.emptyList());

        List<String> itemLore = meta.getLore();
        lore.accept(itemLore);

        meta.setLore(itemLore);
        setMeta();

        return this;
    }

    public ItemBuilder translateColors() {
        acquireMeta();
        if (meta.hasDisplayName()) {
            meta.setDisplayName(Formatter.formatColors(meta.getDisplayName()));
        }
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            assert lore != null;
            lore.replaceAll(Formatter::formatColors);

            meta.setLore(lore);
        }
        setMeta();
        return this;
    }

    public ItemMeta meta() {
        acquireMeta();
        return this.meta;
    }

    public <T extends ItemMeta> T metaAs(Class<T> clazz) {
        acquireMeta();
        return clazz.cast(meta);
    }

    public <T extends ItemMeta> ItemBuilder meta(Class<T> meta, Consumer<T> consumer) {
        consumer.accept(metaAs(meta));
        setMeta();

        return this;
    }

    public ItemBuilder meta(Consumer<ItemMeta> consumer) {
        acquireMeta();
        consumer.accept(this.meta);
        setMeta();

        return this;
    }

    public ItemBuilder meta(ItemMeta meta) {
        this.meta = meta;
        setMeta();

        return this;
    }

    public ItemBuilder glow(boolean doGlow) {
        if (doGlow) {
            enchant(Enchantment.DEPTH_STRIDER, 0);
            flags(ItemFlag.HIDE_ENCHANTS);
        }

        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        setMeta();
        stack.addUnsafeEnchantment(enchant, level);
        acquireMeta();

        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        acquireMeta();
        meta.addItemFlags(flags);
        setMeta();

        return this;
    }

    public ItemBuilder model(int customModelData) {
        acquireMeta();
        meta.setCustomModelData(customModelData);
        setMeta();

        return this;
    }

    public ItemStack build() {
        acquireMeta();
        stack.setItemMeta(meta);
        return stack.clone();
    }

    private void acquireMeta() {
        if (meta == null)
            refreshMeta();
    }

    private void refreshMeta() {
        meta = stack.getItemMeta();
    }

    private void setMeta() {
        stack.setItemMeta(meta);
    }

}
