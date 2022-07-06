package com.alant7_.util.reflections;

import com.alant7_.util.BukkitPlugin;
import com.alant7_.util.TryCatch;
import com.alant7_.util.nms.MinecraftVersion;

import java.util.function.Supplier;

public class ImplementationProvider<T> {

    private Class<?> result;

    private final Class<T> providedClass;

    public ImplementationProvider(Class<T> providedClass) {
        this.providedClass = providedClass;
    }

    public ImplementationProvider<T> ifConditionMet(Supplier<Boolean> condition, String classPath) {
        if (result != null) return this;
        if (condition.get()) {
            return ifClassPresent(classPath);
        }
        return this;
    }

    public ImplementationProvider<T> ifMinecraftVersion(BukkitPlugin plugin, MinecraftVersion version, String classPath) {
        if (result != null) return this;
        return ifConditionMet(() -> plugin.getMinecraftVersion() == version, classPath);
    }

    /**
     * Checks for placeholders
     * [version] - {@link MinecraftVersion}
     */
    public ImplementationProvider<T> ifClassPresent(BukkitPlugin plugin, String classPath) {
        return ifClassPresent(classPath.replace("[version]", plugin.getMinecraftVersion().name()));
    }

    public ImplementationProvider<T> ifClassPresent(String classPath) {
        if (result != null) return this;
        try {
            var clazz = Class.forName(classPath);
            if (providedClass.isAssignableFrom(clazz)) {
                result = clazz;
            }
        } catch (Exception ignored) {
        }

        return this;
    }

    public ImplementationProvider<T> ifNothingFound(Class<T> t) {
        if (this.result == null)
            this.result = t;
        return this;
    }

    @SuppressWarnings("unchecked")
    public final T instantiate(Object... params) {
        Pair<Class<?>, Object>[] constructorParams = new Pair[params.length];
        for (int i = 0; i < params.length; i++) {
            constructorParams[i] = Pair.of(params[i].getClass(), params[i]);
        }
        return instantiate(constructorParams);
    }

    @SafeVarargs
    public final T instantiate(Pair<Class<?>, Object>... params) {
        if (result == null)
            return null;

        try {
            return _instantiate(params);
        } catch (Exception ignored) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    private T _instantiate(Pair<Class<?>, Object>... params) throws Exception {
        Class<?>[] constructorParamTypes = new Class[params.length];
        Object[] constructorParamValues = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            constructorParamTypes[i] = params[i].getKey();
            constructorParamValues[i] = params[i].getValue();
        }

        var constructor = result.getConstructor(constructorParamTypes);
        return (T) constructor.newInstance(constructorParamValues);
    }

}
