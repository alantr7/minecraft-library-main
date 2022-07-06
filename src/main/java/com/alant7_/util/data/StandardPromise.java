package com.alant7_.util.data;

import com.alant7_.util.BukkitPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StandardPromise<T, E> implements Promise<T, E> {

    private Consumer<T> then;

    private Consumer<E> error;

    private BiConsumer<T, E> process;

    private T lastResolved;

    private E lastRejected;

    private boolean isFulfilled = false;

    public StandardPromise() {
    }

    public StandardPromise(T resolved) {
        accept(resolved, null, true);
    }

    public StandardPromise(BiConsumer<Consumer<T>, Consumer<E>> promise) {
        promise.accept(this::resolve, this::reject);
    }

    public synchronized void resolve(T t) {
        accept(t, lastRejected, true);
    }

    public synchronized void reject(E e) {
        accept(lastResolved, e, false);
    }

    private synchronized void accept(T t, E e, boolean isResolve) {
        this.isFulfilled = true;
        if (isResolve) {
            this.lastResolved = t;
            if (then != null)
                then.accept(t);
        } else {
            this.lastRejected = e;
            if (error != null)
                error.accept(e);
        }

        if (process != null)
            process.accept(t, e);

        try {
            notifyAll();
        } catch (Exception ignored) {
        }
    }

    @Override
    public Promise<T, E> then(Consumer<T> then) {
        this.then = then;
        if (lastResolved != null) {
            if (then != null)
                then.accept(lastResolved);
            if (process != null)
                process.accept(lastResolved, lastRejected);
        }
        return this;
    }

    @Override
    public Promise<T, E> thenSync(BukkitPlugin plugin, Consumer<T> k) {
        return then(v -> {
            if (Bukkit.isPrimaryThread())
                k.accept(v);
            else {
                Bukkit.getScheduler().runTask(plugin, () -> k.accept(v));
            }
        });
    }

    @Override
    public Promise<T, E> error(Consumer<E> error) {
        this.error = error;
        if (lastRejected != null) {
            if (error != null)
                error.accept(lastRejected);
            if (process != null)
                process.accept(lastResolved, lastRejected);
        }
        return this;
    }

    @Override
    public Promise<T, E> error(Runnable e) {
        return error(error -> e.run());
    }

    @Override
    public Promise<T, E> process(BiConsumer<T, E> process) {
        this.process = process;
        if (isFulfilled)
            process.accept(lastResolved, lastRejected);
        return this;
    }

    @Override
    public Promise<T, E> processSync(BukkitPlugin plugin, BiConsumer<T, E> process) {
        return process((t, e) -> {
            if (Bukkit.isPrimaryThread())
                process.accept(t, e);
            else {
                Bukkit.getScheduler().runTask(plugin, () -> process.accept(t, e));
            }
        });
    }

    @Override
    public boolean isResolved() {
        return lastResolved != null;
    }

    @Override
    public boolean isFulfilled() {
        return this.isFulfilled;
    }

    @Override
    public synchronized @Nullable T await() {
        return await(0);
    }

    @Override
    public synchronized @Nullable T await(long timeout) {
        if (isFulfilled)
            return lastResolved;

        try {
            wait(timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastResolved;
    }

    public void bind(Promise<T, E> promise) {
        promise.then(this::resolve);
        promise.error(this::reject);
    }

}
