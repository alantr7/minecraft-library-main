package com.alant7_.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;

public abstract class PeriodicTask {

    private BukkitTask task;

    protected abstract void tick();
    public void stop() {
        if (task != null && !task.isCancelled())
            task.cancel();
    }

     public final void start(Plugin plugin, boolean async, long delay, long interval) {
        stop();

        if (async) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, delay, interval);
        } else {
            task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, delay, interval);
        }
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static void stopAll(Plugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

}
