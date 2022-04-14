package com.alant7_.util.gui;

import com.alant7_.util.AlanJavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.function.Function;

public abstract class AbstractMultiplePagesGUI extends AbstractGUI {

    private int pageIndex;

    private int pageSize;

    private int valueCount;

    public AbstractMultiplePagesGUI(AlanJavaPlugin plugin, Player player, boolean init, int pageIndex, int pageSize, int valueCount) {
        super(plugin, player, init);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.valueCount = valueCount;
    }

    public void setValueCount(int valueCount) {
        this.valueCount = valueCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getValueCount() {
        return valueCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Deprecated
    @Override
    protected void fill(Inventory inventory) {}

    @Override
    public void open() {
        fill(getInventory(), pageIndex * pageSize, Math.min((pageIndex + 1) * pageSize, valueCount));

        getPlayer().openInventory(getInventory());
        getPlugin().getGuiManager().openGuis.put(getPlayer().getUniqueId(), this);

        onInventoryOpen();
    }

    public boolean hasNextPage() {
        return valueCount >= (pageIndex + 1) * pageSize;
    }

    public boolean hasPreviousPage() {
        return pageIndex > 0;
    }

    protected abstract void fill(Inventory inventory, int from, int to);

}
