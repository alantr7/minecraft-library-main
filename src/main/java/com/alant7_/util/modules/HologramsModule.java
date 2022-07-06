package com.alant7_.util.modules;

import com.alant7_.util.BukkitPlugin;
import com.alant7_.util.PeriodicTask;
import com.alant7_.util.holograms.api.HologramManager;
import com.alant7_.util.reflections.ImplementationProvider;
import com.alant7_.util.reflections.ObjectWrapper;
import com.alant7_.util.reflections.Pair;
import org.bukkit.event.Listener;

public class HologramsModule implements PluginModule {

    private final BukkitPlugin plugin;

    private final HologramManager hologramManager;

    public HologramsModule(BukkitPlugin plugin) {
        this.plugin = plugin;
        this.hologramManager = new ImplementationProvider<>(HologramManager.class)
                .ifClassPresent("com.alant7_.util.holograms.objects.CraftHologramManager")
                .instantiate(Pair.of(BukkitPlugin.class, plugin));
    }

    @Override
    public void enable() {
        var hologramEventListener = new ImplementationProvider<>(Listener.class)
                .ifClassPresent("com.alant7_.util.holograms.listeners.HologramEventListener")
                .instantiate(Pair.of(HologramManager.class, hologramManager));
        plugin.registerEvents(hologramEventListener);

        var hologramUpdateTask = new ImplementationProvider<>(PeriodicTask.class)
                .ifClassPresent("com.alant7_.util.holograms.tasks.HologramUpdateTask")
                .instantiate(Pair.of(HologramManager.class, hologramManager));
        hologramUpdateTask.start(plugin, false, 0, 10);
    }

    @Override
    public void disable() {
        hologramManager.deleteHolograms();
        new ObjectWrapper(hologramManager).runMethod("unregisterListeners");
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

}
