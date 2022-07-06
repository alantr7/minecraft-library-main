package com.alant7_.util.data.watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WatchedObject<T> {

    private T value;

    private final Map<Integer, Consumer<T>> subscribers = new HashMap<>();

    private final Map<Integer, BiConsumer<T, T>> changeSubscribers = new HashMap<>();

    public boolean has() {
        return value != null;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        T old = this.value;
        this.value = value;

        subscribers.forEach((channel, consumer) -> consumer.accept(value));
        changeSubscribers.forEach((channel, consumer) -> consumer.accept(old, value));
    }

    public void subscribe(int channel, Consumer<T> consumer) {
        subscribers.put(channel, consumer);
    }

    public void unsubscribe(int channel, BiConsumer<T, T> biConsumer) {
        changeSubscribers.put(channel, biConsumer);
    }

    public void unsubscribe(int channel) {
        subscribers.remove(channel);
        changeSubscribers.remove(channel);
    }

}
