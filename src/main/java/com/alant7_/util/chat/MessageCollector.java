package com.alant7_.util.chat;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class MessageCollector {

    private final Map<UUID, LinkedList<PendingCollection>> consumers = new ConcurrentHashMap<>();

    public PendingCollection collect(@NotNull Player player, Consumer<String> input) {
        var collection = new PendingCollection(this, input);
        consumers.computeIfAbsent(player.getUniqueId(), v -> new LinkedList<>()).add(collection);

        return collection;
    }

    public PendingCollection priorityCollect(@NotNull Player player, Consumer<String> input) {
        var collection = new PendingCollection(this, input);
        consumers.put(player.getUniqueId(), new LinkedList<>(Collections.singletonList(collection)));

        return collection;
    }

    public void provide(@NotNull Player player, String input) {
        var collections = consumers.get(player.getUniqueId());
        if (collections == null)
            return;

        var collection = collections.removeFirst();
        collection.getConsumer().accept(input);

        if (collections.size() == 0)
            consumers.remove(player.getUniqueId());
    }

    public boolean isCollecting(@NotNull Player player) {
        return consumers.containsKey(player.getUniqueId());
    }

    public Collection<PendingCollection> getPending(@NotNull Player player) {
        return consumers.getOrDefault(player.getUniqueId(), new LinkedList<>());
    }

    public void cancelAll(@NotNull Player player) {
        consumers.remove(player.getUniqueId());
    }

}
