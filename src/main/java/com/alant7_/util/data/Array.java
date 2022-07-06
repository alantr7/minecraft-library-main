package com.alant7_.util.data;

import java.util.function.IntFunction;

@SuppressWarnings("unchecked")
public class Array<E> {

    private final Object[] objects;

    private int size = 0;

    public Array(int maxSize) {
        this.objects = new Object[maxSize];
    }

    public void add(E e) {
        objects[size++] = e;
    }

    public void remove() {
        objects[size--] = null;
    }

    public E get(int index) {
        return (E) objects[index];
    }

    public E[] toArray(IntFunction<E[]> function) {
        var array = function.apply(this.size);
        System.arraycopy(objects, 0, array, 0, array.length);

        return array;
    }

}
