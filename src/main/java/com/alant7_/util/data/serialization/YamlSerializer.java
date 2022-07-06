package com.alant7_.util.data.serialization;

import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.*;

public class YamlSerializer {

    private final Map<Class<?>, Map<Field, SerializableField>> classMap = new HashMap<>();

    @SuppressWarnings("all")
    private final Map<Class<? extends FieldSerializer<Object>>, FieldSerializer<Object>> serializersRegistry = new HashMap<>();

    private boolean isCacheEnabled = true;

    @SuppressWarnings("unchecked")
    public void registerSerializer(FieldSerializer<?> serializer) {
        serializersRegistry.put((Class<? extends FieldSerializer<Object>>) serializer.getClass(), (FieldSerializer<Object>) serializer);
    }

    public void serialize(Object object, ConfigurationSection section) {
        Map<Field, SerializableField> fields = getSerializableFields(object);
        fields.forEach((field, meta) -> {
            try {
                field.setAccessible(true);

                // Serialize lists
                if (List.class.isAssignableFrom(field.getType())) {
                    serializeList((List<?>) field.get(object), meta, section);
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    serializeMap((Map<?, ?>) field.get(object), meta, section);
                } else {
                    serializeField(field.get(object), meta, meta.name(), section);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("all")
    public void deserialize(ConfigurationSection section, Object object) {
        Map<Field, SerializableField> fields = getSerializableFields(object);
        fields.forEach((field, meta) -> {
            Object value = section.get(meta.name());
            if (value == null)
                return;

            try {
                field.setAccessible(true);
                Object deserialized = List.class.isAssignableFrom(field.getType())
                        ? deserializeList(field, meta, section)
                        : Map.class.isAssignableFrom(field.getType())
                        ? deserializeMap(field, meta, section)
                        : deserializeField(field, meta, meta.name(), section);

                if (deserialized == null)
                    return;

                field.set(object, deserialized);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void serializeList(List<?> objects, SerializableField meta, ConfigurationSection section) {
        section.set(meta.name(), null);

        for (int i = 0; i < objects.size(); i++) {
            Object item = objects.get(i);
            serializeField(item, meta, meta.name() + "." + i, section);
        }
    }

    private void serializeMap(Map<?, ?> map, SerializableField metae, ConfigurationSection section) {
        // TODO
    }

    private void serializeField(Object object, SerializableField meta, String key, ConfigurationSection section) {
        // Serialize normal objects
        if (meta.type() == DataType.ENUM) {
            section.set(meta.name(), ((Enum<?>) object).name());
        } else {
            FieldSerializer<Object> serializer = meta.type() != DataType.CUSTOM ? meta.type().getSerializer() : serializersRegistry.get(meta.serializer());
            if (serializer != null) {
                serializer.serialize(section, key, object);
            }
        }
    }

    private Object deserializeList(Field field, SerializableField meta, ConfigurationSection section) {
        List<Object> list = new LinkedList<>();

        ConfigurationSection listSection = section.getConfigurationSection(meta.name());
        if (listSection == null) {
            return null;
        }

        for (String key : listSection.getKeys(false)) {
            list.add(deserializeField(field, meta, key, listSection));
        }
        return list;
    }

    private Object deserializeMap(Field field, SerializableField meta, ConfigurationSection section) {
        // TODO
        return null;
    }

    @SuppressWarnings("all")
    private Object deserializeField(Field field, SerializableField meta, String key, ConfigurationSection section) {
        Object value = section.get(key);
        if (value == null) {
            return null;
        }

        // Serialize normal objects
        if (meta.type() == DataType.ENUM) {
            return Enum.valueOf((Class<Enum>) field.getType(), (String) value);
        } else {
            FieldSerializer<Object> serializer = meta.type() != DataType.CUSTOM ? meta.type().getSerializer() : serializersRegistry.get(meta.serializer());
            if (serializer != null) {
                return serializer.deserialize(section, key);
            }
        }
        return null;
    }

    private Map<Field, SerializableField> getSerializableFields(Object object) {
        if (isCacheEnabled && classMap.containsKey(object.getClass()))
            return classMap.get(object.getClass());

        Map<Field, SerializableField> fields = new HashMap<>();
        Class<?> currentClass = object.getClass();
        while (currentClass.getSuperclass() != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(SerializableField.class)) {
                    continue;
                }

                SerializableField serializable = field.getAnnotation(SerializableField.class);
                fields.put(field, serializable);
            }

            currentClass = currentClass.getSuperclass();
        }

        if (isCacheEnabled) {
            classMap.put(object.getClass(), fields);
        }
        return fields;
    }

    public boolean isCacheEnabled() {
        return isCacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        isCacheEnabled = cacheEnabled;
    }

}
