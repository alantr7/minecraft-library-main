package com.alant7_.util.nms.items;

import com.alant7_.util.nms.NMSUtil;
import com.alant7_.util.reflections.Pair;
import com.alant7_.util.reflections.ReflectionsUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NMSTagCompound {

    Object nmsTag;

    public NMSTagCompound(Object nmsTag) {
        if (nmsTag == null) {
            try {
                nmsTag = getNBTTagCompoundClass().newInstance();
            } catch (Exception e) {}
        }
        this.nmsTag = nmsTag;
    }

    public NMSTagCompound() {
        this(null);
    }

    @SafeVarargs
    public final <T> T originalMethod(String method, Pair<Class<?>, Object>... params) {
        return ReflectionsUtil.invokeMethod(nmsTag, method, params);
    }

    public boolean hasKey(String s) {
        return originalMethod("hasKey", new Pair<>(String.class, s));
    }

    public Set<String> getKeys() {
        return originalMethod("getKeys");
    }

    private void setObject(String method, String key, Class<?> clazz, Object value) {
        ReflectionsUtil.invokeMethod(nmsTag, method, new Pair<>(String.class, key), new Pair<>(clazz, value));
    }

    private Object getObject(String method, String key) {
        return originalMethod(method, new Pair<>(String.class, key));
    }

    public void setString(String key, String value) {
        setObject("setString", key, String.class, value);
    }

    public void setBoolean(String key, boolean value) {
        setObject("setBoolean", key, boolean.class, value);
    }

    public void setInt(String key, int value) {
        setObject("setInt", key, int.class, value);
    }

    public void setIntArray(String key, int[] value) {
        setObject("setIntArray", key, int[].class, value);
    }

    public void setLong(String key, long value) {
        setObject("setLong", key, long.class, value);
    }

    public void setUUID(String key, UUID uuid) { setString(key, uuid.toString()); }

    public void setEnum(String key, Enum<?> clazz) {
        setString(key, clazz.name());
    }

    public void setShort(String key, short value) {
        setObject("setShort", key, short.class, value);
    }

    public String getString(String key) {
        return (String) getObject("getString", key);
    }

    public boolean getBoolean(String key) {
        return (boolean) getObject("getBoolean", key);
    }

    public int getInt(String key) {
        return (int) getObject("getInt", key);
    }

    public long getLong(String key) {
        return (long) getObject("getLong", key);
    }

    public UUID getUUID(String key) {
        return UUID.fromString(getString(key));
    }

    public <T extends Enum<T>> T getEnum(String key, Class<T> enumType) {
        String string = getString(key);
        return string != null ? T.valueOf(enumType, string) : null;
    }

    public int[] getIntArray(String key) {
        return (int[]) getObject("getIntArray", key);
    }

    public short getShort(String key) {
        return (short) getObject("getShort", key);
    }

    public void remove(String key) {
        originalMethod("remove", new Pair<>(String.class, key));
    }

    @Override
    public String toString() {
        return originalMethod("toString");
    };

    public static Class<?> getNBTTagCompoundClass() {
        return ReflectionsUtil.findClass(
                "net.minecraft.server." + NMSUtil.getVersion() + ".NBTTagCompound"
        );
    }

}
