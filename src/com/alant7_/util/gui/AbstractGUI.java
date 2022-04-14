package com.alant7_.util.gui;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.config.gui.GuiConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGUI {

    private final AlanJavaPlugin plugin;

    private Player player;

    private Inventory inventory;

    private String title;

    private final HashMap<Integer, Map<com.alant7_.util.gui.ClickType, Runnable>> callbackMap = new HashMap<>();

    private final HashMap<Action, List<Runnable>> eventCallbackMap = new HashMap<>();

    private boolean isDisposed = false;

    private boolean isCancelled = false;

    private boolean isItemInteractionEnabled = true;

    private InventoryClickEvent event;

    private CloseInitiator closeInitiator;

    public AbstractGUI(AlanJavaPlugin plugin, Player player) {
        this(plugin, player, true);
    }

    public AbstractGUI(AlanJavaPlugin plugin, Player player, boolean init) {
        this.plugin = plugin;
        this.player = player;
        if (init) {
            init();
        }
    }

    protected abstract void init();

    protected abstract void fill(Inventory inventory);

    protected void createWith(GuiConfiguration config) {
    }

    public void refill() {
        getInventory().clear();
        fill(getInventory());
    }

    protected void createInventory(String title, int size) {
        this.title = title;
        inventory = Bukkit.createInventory(player, size, title);
    }

    protected void createInventory(String title, InventoryType type) {
        this.title = title;
        inventory = Bukkit.createInventory(player, type, title);
    }

    protected void createInventory(Inventory inventory) {
        this.title = "";
        this.inventory = inventory;
    }

    public void open() {

        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, this::open);
            return;
        }

        if (plugin.getGuiManager().getEventListener().isInEvent(getPlayer())) {

            Bukkit.getScheduler().runTaskLater(plugin, this::open, 1);

            return;

        }

        fill(getInventory());

        getPlayer().openInventory(getInventory());

        plugin.getGuiManager().openGuis.put(player.getUniqueId(), this);

        runEventCallbacks(Action.OPEN);

        onInventoryOpen();

    }

    protected abstract void onInventoryOpen();

    protected abstract void onInventoryClose(CloseInitiator closeInitiator);

    public final void close() {
        close(CloseInitiator.SELF);
    }

    public final void close(CloseInitiator reason) {
        this.closeInitiator = reason;
        if (reason == CloseInitiator.BUKKIT) {
            onInventoryClose(CloseInitiator.BUKKIT);
            runEventCallbacks(Action.CLOSE);
            return;
        }
        getPlayer().closeInventory();
    }

    public void onDisposed() {}

    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public ItemStack getItem(int slot) {
        if (inventory == null) {
            return null;
        }
        if (isOutside(slot)) {
            return null;
        }
        if (hasClickEvent()) {
            return event.getInventory().getSize() > slot
                            ? event.getInventory().getItem(slot) : _getItem(slot);
        }
        return inventory.getSize() > slot ? inventory.getItem(slot) : _getItem(slot);
    }

    private ItemStack _getItem(int rawslot) {
        int slot = rawslot - inventory.getSize() + 9;
        if (slot > 35) {
            slot = slot - 36;
        }

        return player.getInventory().getItem(slot);
    }

    @Deprecated
    public void addLeftClickCallback(int slot, Runnable onItemClicked) {
        registerInteractionCallback(slot, com.alant7_.util.gui.ClickType.LEFT, onItemClicked);
    }

    @Deprecated
    public void addRightClickCallback(int slot, Runnable onItemClicked) {
        registerInteractionCallback(slot, com.alant7_.util.gui.ClickType.RIGHT, onItemClicked);
    }

    public void registerInteractionCallback(int slot, com.alant7_.util.gui.ClickType type, Runnable runnable) {
        Map<com.alant7_.util.gui.ClickType, Runnable> cbs = callbackMap.computeIfAbsent(slot, k -> new HashMap<>());
        cbs.put(type, runnable);
    }

    @Deprecated
    public void removeLeftClickCallbacks(int slot) {
        removeInteractionCallback(slot, com.alant7_.util.gui.ClickType.LEFT);
    }

    @Deprecated
    public void removeRightClickCallbacks(int slot) {
        removeInteractionCallback(slot, com.alant7_.util.gui.ClickType.RIGHT);
    }

    public void clearInteractionCallbacks() {
        callbackMap.forEach((slot, map) -> {
            map.clear();
        });
        callbackMap.clear();
    }

    public void clearInteractionCallbacks(int slot) {
        Map<com.alant7_.util.gui.ClickType, Runnable> listeners = callbackMap.remove(slot);
        if (listeners != null) {
            listeners.clear();
        }
    }

    public void removeInteractionCallback(int slot, com.alant7_.util.gui.ClickType type) {
        Map<com.alant7_.util.gui.ClickType, Runnable> listeners = callbackMap.get(slot);
        if (listeners != null) {
            listeners.remove(type);
        }
    }

    public void registerEventCallback(Action action, Runnable runnable) {
        eventCallbackMap.computeIfAbsent(action, v -> new ArrayList<>()).add(runnable);
    }

    public void clearEventCallbacks() {
        eventCallbackMap.clear();
    }

    public void clearEventCallbacks(Action action) {
        eventCallbackMap.remove(action);
    }

    public abstract void onItemInteract(int slot, @NotNull com.alant7_.util.gui.ClickType clickType, @Nullable ItemStack stack);

    protected void runEventCallbacks(Action action) {
        List<Runnable> cbs = eventCallbackMap.get(action);
        if (cbs != null)
            cbs.forEach(Runnable::run);
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void click(int slot, com.alant7_.util.gui.ClickType clickType) {

        if (inventory == null) {
            return;
        }

        Runnable r = getInteractionCallback(slot, clickType);

        if (r != null)
            r.run();

        onItemInteract(slot, clickType, getItem(event.getRawSlot()));

    }

    private void _interact(int slot, ClickType type, InventoryClickEvent event) {
        if (inventory == null)
            return;
        this.event = event;
        if (!isInteractionEnabled() && event != null)
            event.setCancelled(true);
        if (type.isLeftClick()) {
            this.click(slot, com.alant7_.util.gui.ClickType.LEFT);
        } else if (type.isRightClick()) {
            this.click(slot, com.alant7_.util.gui.ClickType.RIGHT);
        } else if (event != null) {
            event.setCancelled(true);
        }
    }

    void interact(int slot, ClickType type) {
        _interact(slot, type, null);
    }

    void interact(InventoryClickEvent event) {
        _interact(event.getRawSlot(), event.getClick(), event);
    }

    public void cancel() {
        isCancelled = true;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean belongsToPlayer(int slot) {
        return slot >= inventory.getSize() && slot < inventory.getSize() + 36;
    }

    public boolean belongsToGUI(int slot) {
        return inventory != null && slot < inventory.getSize() && slot >= 0;
    }

    public boolean isOutside(int slot) {
        return !belongsToGUI(slot) && !belongsToPlayer(slot);
    }

    public boolean hasClickEvent() {
        return event != null;
    }

    public String getInventoryTitle() {
        return title;
    }

    public int getInventorySize() {
        return inventory.getSize();
    }

    public boolean isInteractionEnabled() {
        return isItemInteractionEnabled;
    }

    public void setInteractionEnabled(boolean itemInteractionEnabled) {
        isItemInteractionEnabled = itemInteractionEnabled;
    }

    public InventoryClickEvent getClickEvent() {
        return event;
    }

    public CloseInitiator getCloseInitiator() {
        return closeInitiator;
    }

    public boolean is(Class<? extends AbstractGUI> gui) {
        return getClass().equals(gui);
    }

    public AlanJavaPlugin getPlugin() {
        return plugin;
    }

    private Runnable getInteractionCallback(int slot, com.alant7_.util.gui.ClickType type) {
        Map<com.alant7_.util.gui.ClickType, Runnable> cbs = callbackMap.get(slot);
        if (cbs == null) {
            return null;
        }
        if (cbs.containsKey(type)) {
            return cbs.get(type);
        }
        if (cbs.containsKey(com.alant7_.util.gui.ClickType.ANY)) {
            return cbs.get(com.alant7_.util.gui.ClickType.ANY);
        }
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AbstractGUI) {
            return ((AbstractGUI) object).inventory.equals(inventory);
        }
        if (object instanceof Inventory) {
            return inventory.equals(object);
        }

        return false;
    }

    public void dispose() {
        clearInteractionCallbacks();

        player = null;
        inventory = null;

        isDisposed = true;
        onDisposed();
    }

    public enum Action {
        OPEN, CLOSE
    }

}
