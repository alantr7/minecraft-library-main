package com.alant7_.util.reflections;

import com.alant7_.util.TryCatch;

import java.lang.reflect.Method;

public class ObjectWrapper {

    private final Object handle;

    private final Class<?> handleClass;

    public ObjectWrapper(Object handle) {
        this.handle = handle;
        this.handleClass = handle.getClass();
    }

    public <T> ObjectWrapper(T t, Class<T> clazz) {
        this.handle = t;
        this.handleClass = clazz;
    }

    public Object runMethod(String name) {
        return runMethod(name, Object.class);
    }

    public <T> T runMethod(String name, Class<T> returnType) {
        try {
            return returnType.cast(_runMethod(name, new Class[0], new Object[0]));
        } catch (Exception e) {
            return null;
        }
    }

    private Object _runMethod(String name, Class<?>[] classes, Object[] values) throws Exception {
        var method = _getMethod(handleClass, name, classes);
        if (method == null) return null;

        method.setAccessible(true);
        return method.invoke(values);
    }

    private Method _getMethod(Class<?> clazz, String name, Class<?>[] classes) {
        try {
            return clazz.getDeclaredMethod(name, classes);
        } catch (Exception ignored) {
            if (clazz.getSuperclass() != Object.class) {
                return _getMethod(clazz.getSuperclass(), name, classes);
            }
            return null;
        }
    }

}
