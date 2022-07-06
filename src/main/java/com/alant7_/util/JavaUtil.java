package com.alant7_.util;

import com.alant7_.util.reflections.Pair;

import java.util.*;
import java.util.function.Supplier;

public class JavaUtil {

    @SafeVarargs
    public static <K, V> Map<K, V> createMap(Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs)
            map.put(pair.getKey(), pair.getValue());

        return map;
    }

    public static class TryCatchResult<T> {

        private Exception e;

        private T result;

        public TryCatchResult(Supplier<T> supplier) {
            try {
                result = supplier.get();
            } catch (Exception e) {
                this.e = e;
            }
        }

        public boolean hasException() {
            return e != null;
        }

        public Exception getException() {
            return e;
        }

        public T get() {
            return result;
        }

    }

}
