package com.alant7_.util.nms;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Repeatable(NmsControllers.class)
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NmsController {

    @NotNull MinecraftVersion version();

    /**
     * Creates an instance of the specified class, if the version matches the server's version
     */
    @NotNull Class<?> implClass() default Object.class;

    /**
     * Find an implementation class at the specified path. If found, and if the version match, the class will be instantiated.
     * {version} placeholder will be replaced with the server's version in the specified path string
     */
    @NotNull String implPath() default "";

}
