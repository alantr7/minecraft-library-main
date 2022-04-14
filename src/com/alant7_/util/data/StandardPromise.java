package com.alant7_.util.data;

import java.util.function.Consumer;

public class StandardPromise<T, E> implements Promise<T, E> {

    private Consumer<T> then;

    private Consumer<E> error;

    public void resolve(T t) {
        if (then != null)
            then.accept(t);
    }

    public void reject(E e) {
        if (error != null)
            error.accept(e);
    }

    @Override
    public void then(Consumer<T> then) {
        this.then = then;
    }

    @Override
    public void error(Consumer<E> error) {
        this.error = error;
    }

}
