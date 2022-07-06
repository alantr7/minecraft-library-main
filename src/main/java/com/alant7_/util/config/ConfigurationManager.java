package com.alant7_.util.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private final Map<Class<?>, Configuration> configurationMap = new HashMap<>();

    public <T extends Configuration> T get(Class<T> clazz) {
        return clazz.cast(configurationMap.get(clazz));
    }

    void register(Configuration configuration) {
        configurationMap.put(configuration.getClass(), configuration);
    }

}
