package com.alant7_.util.data.watcher;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class WatchedList<E> extends ArrayList<E> {

    public static final EventEmpty EVENT_CONTENT_CHANGE = new EventEmpty();

    public static final EventEmpty EVENT_CLEAR = new EventEmpty();

    public static final EventPayload<?> EVENT_ADD = new EventPayload<>();

    public static final EventPayload2<Object, Object> EVENT_SET = new EventPayload2<>();

    public static final EventPayload2<Integer, Object> EVENT_REMOVE = new EventPayload2<>();

    private final Map<EventPayload<?>, Map<Integer, List<Consumer<Object>>>> subscribersPayload = new HashMap<>();

    private final Map<EventPayload2<?, ?>, Map<Integer, List<BiConsumer<Object, Object>>>> subscribersPayload2 = new HashMap<>();

    private final Map<EventEmpty, Map<Integer, List<Runnable>>> subscribersNoPayload = new HashMap<>();

    @Override
    public boolean add(E e) {
        notify(EVENT_CONTENT_CHANGE);
        notify(EVENT_ADD, e);
        return super.add(e);
    }

    @Override
    public E set(int index, E element) {
        E old = super.set(index, element);
        notify(EVENT_SET, old, element);
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int indexOf = indexOf(o);
        if (indexOf == -1)
            return false;

        notify(EVENT_CONTENT_CHANGE);
        notify(EVENT_REMOVE, indexOf, o);

        return true;
    }

    @Override
    public E remove(int index) {
        E value = super.remove(index);
        notify(EVENT_REMOVE, index, value);

        return value;
    }

    @Override
    public void clear() {
        super.clear();
        notify(EVENT_CONTENT_CHANGE);
        notify(EVENT_CLEAR);
    }

    public void subscribe(EventEmpty event, int channel, Runnable callback) {
        subscribersNoPayload.computeIfAbsent(event, v -> new HashMap<>()).computeIfAbsent(channel, v -> new LinkedList<>()).add(callback);
    }

    public void subscribe(EventPayload<?> event, int channel, Consumer<E> payload) {
        subscribersPayload.computeIfAbsent(event, v -> new HashMap<>()).computeIfAbsent(channel, v -> new LinkedList<>()).add((Consumer<Object>) payload);
    }

    public <V1, V2> void subscribe(EventPayload2<V1, V2> event, int channel, BiConsumer<V1, V2> payload) {
        subscribersPayload2.computeIfAbsent(event, v -> new HashMap<>()).computeIfAbsent(channel, v -> new LinkedList<>()).add((BiConsumer<Object, Object>) payload);
    }

    public void unsubscribe(int channel) {
        unsubscribe(EVENT_ADD, channel);
        unsubscribe(EVENT_REMOVE, channel);
        unsubscribe(EVENT_CONTENT_CHANGE, channel);
    }

    public void unsubscribe(EventEmpty event, int channel) {
        unsubscribe(subscribersNoPayload, event, channel);
    }

    public void unsubscribe(EventPayload<?> event, int channel) {
        unsubscribe(subscribersPayload, event, channel);
    }

    public void unsubscribe(EventPayload2<?, ?> event, int channel) {
        unsubscribe(subscribersPayload2, event, channel);
    }

    private <K extends Event, V> void unsubscribe(Map<K, Map<Integer, V>> map, K event, int channel) {
        Map<Integer, V> subscribers = map.get(event);
        if (subscribers != null) {
            subscribers.remove(channel);
        }
    }

    private void notify(EventEmpty event) {
        notify(event, subscribersNoPayload, Runnable::run);
    }

    private void notify(EventPayload<?> event, E payload) {
        notify(event, subscribersPayload, consumer -> consumer.accept(payload));
    }

    private <V1, V2> void notify(EventPayload2<V1, V2> event, V1 v1, V2 v2) {
        notify(event, subscribersPayload2, consumer -> consumer.accept(v1, v2));
    }

    private <EventClass extends Event, V> void notify(EventClass event, Map<EventClass, Map<Integer, List<V>>> map, Consumer<V> notify) {
        Map<Integer, List<V>> channels = map.get(event);
        if (channels == null)
            return;

        channels.forEach((channel, list) -> {
            for (V v : list) {
                notify.accept(v);
            }
        });
    }

    private static class Event {
    }

    static class EventPayload<V> extends Event {
    }

    static class EventPayload2<V1, V2> extends Event {
    }

    static class EventEmpty extends Event {
    }

}
