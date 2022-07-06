package com.alant7_.util.config.updater;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.function.BiConsumer;

public class UpdateAction<T> {

    private final T t;

    public UpdateAction(T t) {
        this.t = t;
    }

    T getExecutor() {
        return t;
    }

    @FunctionalInterface
    public interface UpdateActionAdd {
        void perform(FileConfiguration internal, ConfigurationSection config, String key);
    }

    @FunctionalInterface
    public interface UpdateActionMove {
        void perform(ConfigurationSection config, String key, String newKey);
    }

    @FunctionalInterface
    public interface UpdateActionRemove {
        void perform(ConfigurationSection config, String key);
    }

}
