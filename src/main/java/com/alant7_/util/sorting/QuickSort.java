package com.alant7_.util.sorting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuickSort {

    public static <T extends Sortable> List<T> sort(Collection<T> list) {

        ArrayList<T> items = new ArrayList<>(list);
        quickSort(items, 0, items.size() - 1);

        return items;

    }

    static <T extends Sortable> int partition(ArrayList<T> array, int begin, int end) {
        int pivot = end;

        int counter = begin;
        for (int i = begin; i < end; i++) {
            if (array.get(i).greaterThan(array.get(pivot))) {
                T temp = array.get(counter);
                array.set(counter, array.get(i));
                array.set(i, temp);
                counter++;
            }
        }

        T temp = array.get(pivot);
        array.set(pivot, array.get(counter));
        array.set(counter, temp);

        return counter;
    }

    private static <T extends Sortable> void quickSort(ArrayList<T> array, int begin, int end) {
        if (end <= begin) return;
        int pivot = partition(array, begin, end);

        quickSort(array, begin, pivot-1);
        quickSort(array, pivot+1, end);
    }

}
