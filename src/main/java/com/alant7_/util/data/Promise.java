package com.alant7_.util.data;

import com.alant7_.util.BukkitPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Promise<K, E> {

    Promise<K, E> then(Consumer<K> k);

    Promise<K, E> thenSync(BukkitPlugin plugin, Consumer<K> k);

    Promise<K, E> error(Consumer<E> e);

    Promise<K, E> error(Runnable e);

    Promise<K, E> process(BiConsumer<K, E> process);

    Promise<K, E> processSync(BukkitPlugin plugin, BiConsumer<K, E> process);

    boolean isResolved();

    boolean isFulfilled();

    @Nullable K await();

    @Nullable K await(long timeout);

    static <K, E> Promise<K, E> resolved(K k) {
        return new StandardPromise<>(k);
    }

    static <K, E> Promise<K, E> rejected(E e) {
        StandardPromise<K, E> promise = new StandardPromise<>();
        promise.reject(e);

        return promise;
    }

    static <K, E> StandardPromise<K, E> create() {
        return new StandardPromise<>();
    }

}
