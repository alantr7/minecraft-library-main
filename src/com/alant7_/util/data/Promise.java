package com.alant7_.util.data;

import java.util.function.Consumer;

public interface Promise<K, E> {

    void then(Consumer<K> k);

    void error(Consumer<E> e);

}
