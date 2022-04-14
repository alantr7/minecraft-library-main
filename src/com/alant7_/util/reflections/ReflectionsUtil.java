package com.alant7_.util.reflections;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class ReflectionsUtil {

    public static <T> T invokeMethod(Object object, String method, Pair<Class<?>, Object>... params) {
        try {
            Class<?>[] classes = new Class<?>[params.length];
            Object[] values = new Object[params.length];

            for (int i = 0; i < params.length; i++) {
                classes[i] = params[i].getKey();
                values[i] = params[i].getValue();
            }

            Method methodObject = object.getClass().getMethod(method, classes);

            return (T) methodObject.invoke(object, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeMethod(Object object, String method, Object... params) {
        try {
            Class<?>[] classes = new Class<?>[params.length];
            Object[] values = new Object[params.length];

            for (int i = 0; i < params.length; i++) {
                classes[i] = params[i].getClass();
                values[i] = params[i];
            }

            Method methodObject = object.getClass().getMethod(method, classes);
            return (T) methodObject.invoke(object, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeMethod(Class<?> object, String method, Pair<Class<?>, Object>... params) {
        try {
            Class<?>[] classes = new Class<?>[params.length];
            Object[] values = new Object[params.length];

            for (int i = 0; i < params.length; i++) {
                classes[i] = params[i].getKey();
                values[i] = params[i].getValue();
            }

            Method methodObject = object.getMethod(method, classes);
            return (T) methodObject.invoke(null, values);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T invokeMethod(Class<?> object, String method, Object... params) {
        try {
            Class<?>[] classes = new Class<?>[params.length];
            Object[] values = new Object[params.length];

            for (int i = 0; i < params.length; i++) {
                classes[i] = params[i].getClass();
                values[i] = params[i];
            }

            Method methodObject = object.getMethod(method, classes);
            return (T) methodObject.invoke(null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Class<?> findClass(String path) {
        try {
            return Class.forName(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object orDefault(Object o1, Object o2) {
        return o1 == null ? o2 : o1;
    }

}
