package com.alant7_.util.data.serialization;

import com.alant7_.util.TriConsumer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.BiFunction;

public class FieldSerializer<T> {

    private final TriConsumer<ConfigurationSection, String, T> serializer;

    private final BiFunction<ConfigurationSection, String, T> deserializer;

    public FieldSerializer(TriConsumer<ConfigurationSection, String, T> serializer, BiFunction<ConfigurationSection, String, T> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public void serialize(ConfigurationSection section, String key, T t) {
        serializer.accept(section, key, t);
    }

    public T deserialize(ConfigurationSection section, String key) {
        return deserializer.apply(section, key);
    }

    public static class NULL extends FieldSerializer {

        public NULL(TriConsumer<ConfigurationSection, String, ?> serializer, BiFunction<ConfigurationSection, String, ?> deserializer) {
            super(serializer, deserializer);
        }

    }

}
