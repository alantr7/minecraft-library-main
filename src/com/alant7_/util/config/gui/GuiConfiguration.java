package com.alant7_.util.config.gui;

import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;

public class GuiConfiguration {

    private InventoryType inventoryType;

    private String title;

    private int inventorySize;

    private final List<GuiConfigurationItem> items = new ArrayList<>();

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public String getTitle() {
        return title;
    }

    public int getInventorySize() {
        return inventorySize;
    }

}
