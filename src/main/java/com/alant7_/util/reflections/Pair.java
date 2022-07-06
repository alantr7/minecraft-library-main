package com.alant7_.util.reflections;

public class Pair<K, V> {

    private K K;
    private V V;

    public Pair() {
        this.K = null;
        this.V = null;
    }

    public Pair(K k, V v) {
        this.K = k;
        this.V = v;
    }

    public K getKey() {
        return K;
    }

    public V getValue() {
        return V;
    }

    public void setKey(K k) {
        K = k;
    }

    public void setValue(V v) {
        V = v;
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

}
