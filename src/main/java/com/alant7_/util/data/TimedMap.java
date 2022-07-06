package com.alant7_.util.data;

import java.util.*;

public class TimedMap<K, V> {

    private final Map<K, V> map = new LinkedHashMap<>();

    private final Map<K, Long> timestamps = new LinkedHashMap<>();

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.containsValue(value);
    }

    public V get(Object key) {
        return map.get(key);
    }

    public V put(K key, V value) {
        timestamps.put(key, System.currentTimeMillis());
        return map.put(key, value);
    }

    public V remove(Object key) {
        timestamps.remove(key);
        return map.remove(key);
    }

    public void removeOlderThan(long millis) {

    }

    public long getTimestamp(K key) {
        return timestamps.get(key);
    }

    public void clear() {
        map.clear();
        timestamps.clear();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

}
