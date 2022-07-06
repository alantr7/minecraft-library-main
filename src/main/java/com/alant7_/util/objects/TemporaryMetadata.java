package com.alant7_.util.objects;

import java.util.HashMap;
import java.util.Map;

public class TemporaryMetadata {

    private Map<String, Object> objects = new HashMap<>();

    public Object getObject(String key) {
        return objects.get(key);
    }

    public boolean hasKey(String key) {
        return objects.containsKey(key);
    }

    public void setObject(String key, Object object) {
        objects.put(key, object);
    }

    public void clear() {
        objects.clear();
    }

}
