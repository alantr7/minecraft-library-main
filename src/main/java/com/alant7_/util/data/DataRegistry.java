package com.alant7_.util.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataRegistry<K, V> {

    protected final Map<K, V> items = new LinkedHashMap<>();

    public void register(K k, V v) {
        items.put(k, v);
    }

    public boolean isRegistered(K k) {
        return items.containsKey(k);
    }

    public void unregister(K k) {
        items.remove(k);
    }

    public V getItem(K k) {
        return items.get(k);
    }

    @SuppressWarnings("unchecked")
    public UnmodifiableList<V> getItems() {
        return new UnmodifiableList<>(new ArrayList<>(items.values()));
    }

    @SafeVarargs
    private V[] createEmptyArray(V... v) {
        return v;
    }

}
