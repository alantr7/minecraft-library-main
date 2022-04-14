package com.alant7_.util.holograms;

import org.bukkit.inventory.ItemStack;

public class HologramLineContent {

    private String text;

    private ItemStack stack;

    public HologramLineContent(String text) {
        this.text = text;
    }

    public HologramLineContent(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getItemStack() {
        return stack;
    }

    public void setItemStack(ItemStack stack) {
        this.stack = stack;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getContent() {
        return stack != null ? stack : text;
    }

}
