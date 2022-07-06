package com.alant7_.util.event.listeners;

import com.alant7_.util.BukkitPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListenerMessageCollector implements Listener {

    private final BukkitPlugin plugin;

    public EventListenerMessageCollector(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        if (plugin.getMessageCollector().isCollecting(event.getPlayer())) {
            event.setCancelled(true);
            plugin.getMessageCollector().provide(event.getPlayer(), event.getMessage());
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var pending = plugin.getMessageCollector().getPending(event.getPlayer());
        pending.removeIf(p -> !p.isPersistent());

        if (pending.size() == 0) {
            plugin.getMessageCollector().cancelAll(event.getPlayer());
        }
    }

}
