package com.alant7_.util.data.serialization;

import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
                    List<Object> list = new ArrayList<>();
                    List<?> objects = (List<?>) field.get(object);
                    if (meta.type() == DataType.ENUM) {
                        for (Object obj : objects) {
                            list.add(((Enum<?>) obj).name());
                        }
                        section.set(meta.name(), list);
                    } else {
                        FieldSerializer<Object> serializer = meta.type() != DataType.CUSTOM ? meta.type().getSerializer() : serializersRegistry.get(meta.serializer());
                        if (serializer != null) {
                            for (int i = 0; i < objects.size(); i++) {
                                ConfigurationSection itemSection = section.createSection(String.valueOf(i));
                                serializer.serialize(itemSection, meta.name(), objects.get(i));
                            }
                        }
                    }
                } else {
                    // Serialize normal objects
                    if (meta.type() == DataType.ENUM) {
                        section.set(meta.name(), ((Enum<?>) field.get(object)).name());
                    } else {
                        FieldSerializer<Object> serializer = meta.type() != DataType.CUSTOM ? meta.type().getSerializer() : serializersRegistry.get(meta.serializer());
                        if (serializer != null) {
                            serializer.serialize(section, meta.name(), field.get(object));
                        }
                    }
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
            if (value == null) {
                return;
            }

            try {

                field.setAccessible(true);

                FieldSerializer<Object> serializer = meta.type() != DataType.CUSTOM ? meta.type().getSerializer() : serializersRegistry.get(meta.serializer());
                Object deserialized = null;

                if (serializer != null) {
                    deserialized = serializer.deserialize(section, meta.name());
                }

                if (meta.type() == DataType.ENUM) {
                    field.set(object, Enum.valueOf((Class<Enum>) field.getType(), (String) value));
                    return;
                }

                field.set(object, deserialized);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
