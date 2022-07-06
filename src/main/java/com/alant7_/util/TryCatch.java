package com.alant7_.util;

import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;

@FunctionalInterface
public interface TryCatch<E> {

    E run() throws Exception;

    static <E> E perform(TryCatch<E> throwable) {
        return perform(throwable, (E) null);
    }

    @Contract("!null, !null -> !null")
    static <E> E perform(TryCatch<E> throwable, E defaultValue) {
        try {
            return throwable.run();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    static <E> E perform(TryCatch<E> throwable, Consumer<Exception> exception) {
        try {
            return throwable.run();
        } catch (Exception e) {
            exception.accept(e);
        }
        return null;
    }

    static void perform(TryCatchVoid throwable) {
        perform(throwable, Exception::printStackTrace);
    }

    static void perform(TryCatchVoid throwable, Consumer<Exception> exceptionHandler) {
        try {
            throwable.run();
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

    @FunctionalInterface
    interface TryCatchVoid {

        void run() throws Exception;

    }

}
