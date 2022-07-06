package com.alant7_.util.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class UnmodifiableList<T> implements Iterable<T> {

    private final List<T> list;

    public UnmodifiableList(List<T> list) {
        this.list = list;
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new UnmodifiableListIterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    private class UnmodifiableListIterator implements Iterator<T> {

        private int index = -1;

        @Override
        public boolean hasNext() {
            return index + 1 < list.size();
        }

        @Override
        public T next() {
            return list.get(++index);
        }

    }

}
