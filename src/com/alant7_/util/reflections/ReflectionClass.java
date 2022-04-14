package com.alant7_.util.reflections;

public abstract class ReflectionClass {

    public Object callMethod(String name, Object... params) {
        return ReflectionsUtil.invokeMethod(this, name, params);
    }

}
