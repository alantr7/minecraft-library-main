package com.alant7_.util;

import java.util.HashMap;
import java.util.Map;

public class PluginSettings {

    public enum Feature {

        GUI,
        HOLOGRAM,
        PLAYER_DATA_MANAGER,
        YAML_SERIALIZER

    }

    public enum EventListener {

        ARMOR_EQUIP,
        BREWING,

    }

    private final boolean[] features = new boolean[Feature.values().length];

    private final boolean[] listeners = new boolean[EventListener.values().length];

    private boolean locked = false;

    public void toggleFeature(Feature feature, boolean value) {
        if (!locked)
            features[feature.ordinal()] = value;
        else throw new RuntimeException("Plugin's settings can only be changed during the setup phase.");
    }

    public void toggleEventListener(EventListener listener, boolean value) {
        if (!locked)
            listeners[listener.ordinal()] = value;
        else throw new RuntimeException("Plugin's settings can only be changed during the setup phase.");
    }

    public boolean isFeatureEnabled(Feature feature) {
        return features[feature.ordinal()];
    }

    public boolean isEventListenerEnabled(EventListener listener) {
        return listeners[listener.ordinal()];
    }

    void lock() {
        locked = true;
    }

}
