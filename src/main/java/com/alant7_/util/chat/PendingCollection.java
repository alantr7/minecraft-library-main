package com.alant7_.util.chat;

import java.util.function.Consumer;

public class PendingCollection {

    private final MessageCollector collector;

    private final Consumer<String> consumer;

    private boolean isCancelled = false;

    private boolean isPersistent = false;

    public PendingCollection(MessageCollector collector, Consumer<String> consumer) {
        this.collector = collector;
        this.consumer = consumer;
    }

    Consumer<String> getConsumer() {
        return consumer;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    /**
     * @return A value that hints whether the collector persists when player relogs
     */
    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean persistent) {
        isPersistent = persistent;
    }

}
