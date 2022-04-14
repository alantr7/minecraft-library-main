package com.alant7_.util.data;

import org.bukkit.Bukkit;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TimedList<E> implements List<E> {

    private Object[] data;

    private long[] timestamps;

    public TimedList(TimedList<E> list) {
        this.data = list.data;
        this.timestamps = list.timestamps;
    }

    public TimedList() {
        this.data = new Object[0];
        this.timestamps = new long[0];
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public boolean isEmpty() {
        return data.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new CraftListIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        T[] t = Arrays.copyOf(a, data.length);
        for (int i = 0; i < data.length; i++)
            t[i] = (T) data[i];
        return t;
    }

    @Override
    public boolean add(E e) {
        return add(e, System.currentTimeMillis());
    }

    public boolean add(E e, long timestamp) {
        this.data = Arrays.copyOf(data, data.length + 1);
        this.data[data.length - 1] = e;

        this.timestamps = Arrays.copyOf(timestamps, timestamps.length + 1);
        this.timestamps[timestamps.length - 1] = timestamp;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        return remove(indexOf(o)) != null;
    }

    public void forEach(BiConsumer<? super E, Long> action) {
        int index = 0;
        for (Object e : data) {
            action.accept((E) e, timestamps[index++]);
        }
    }

    public void removeOlderThan(long millis) {
        long time = System.currentTimeMillis();
        int from = 0;
        for (int i = 0; i < timestamps.length; i++) {
            if (time - timestamps[i] < millis) {
                from = i;
                break;
            }
        }

        if (from == 0)
            return;

        TimedList<E> copy = (TimedList<E>) subList(from, data.length);
        this.data = copy.data;
        this.timestamps = copy.timestamps;
    }

    public long getTimestamp(E object) {
        int index = indexOf(object);
        return index == -1 ? 0 : timestamps[index];
    }

    public long getTimestamp(int index) {
        return timestamps[index];
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c)
            add(e);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c)
            add(index++, e);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c)
            remove(o);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        this.data = new Object[0];
        this.timestamps = new long[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) data[index];
    }

    @Override
    public E set(int index, E element) {
        data[index] = element;
        return element;
    }

    @Override
    public void add(int index, E element) {
        Object[] data1 = new Object[data.length];
        long[] timestamps2 = new long[timestamps.length];

        for (int i = 0; i < index; i++) {
            data1[i] = data[index];
            timestamps2[i] = timestamps[i];
        }
        data1[index] = element;
        timestamps2[index] = System.currentTimeMillis();
        for (int i = index + 1; i < data1.length; i++) {
            data1[i] = data[index - 1];
            timestamps2[i] = timestamps[index - 1];
        }
        this.data = data1;
        this.timestamps = timestamps2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= this.data.length)
            return null;
        Object[] data1 = new Object[data.length - 1];
        long[] timestamps1 = new long[data.length - 1];
        for (int i = 0; i < index; i++) {
            data1[i] = data[i];
            timestamps1[i] = timestamps[i];
        }
        E e = (E) this.data[index];
        for (int i = index + 1; i < data.length; i++) {
            data1[i - 1] = data[i];
            timestamps1[i - 1] = timestamps[i - 1];
        }
        this.data = data1;
        this.timestamps = timestamps1;
        return e;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < data.length; i++)
            if (data[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = data.length - 1; i >= 0; i--)
            if (data[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new CraftListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new CraftListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        TimedList<E> sublist = new TimedList<>();

        sublist.data = Arrays.copyOfRange(this.data, fromIndex, toIndex);
        sublist.timestamps = Arrays.copyOfRange(this.timestamps, fromIndex, toIndex);

        return sublist;
    }

    public class CraftListIterator implements ListIterator<E> {

        private int index;

        CraftListIterator(int index) {
            this.index = index;
        }

        CraftListIterator() {
            this(0);
        }

        @Override
        public boolean hasNext() {
            return index + 1 < data.length;
        }

        @Override
        public E next() {
            return get(++index);
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public E previous() {
            return get(--index);
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            TimedList.this.remove(index);
        }

        @Override
        public void set(E e) {
            TimedList.this.set(index, e);
        }

        @Override
        public void add(E e) {
            TimedList.this.add(index, e);
        }

    }

}
