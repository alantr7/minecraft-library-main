package com.alant7_.util.objects;

public class Holder<T> {

    private T value;

    public Holder() {
    }

    public Holder(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T t) {
        value = t;
    }

    public boolean has() {
        return value != null;
    }

}
