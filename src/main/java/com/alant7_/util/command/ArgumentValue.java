package com.alant7_.util.command;

public class ArgumentValue<T> {

    private final T value;

    protected ArgumentValue(T t) {
        value = t;
    }

    public T get() {
        return value;
    }

}
