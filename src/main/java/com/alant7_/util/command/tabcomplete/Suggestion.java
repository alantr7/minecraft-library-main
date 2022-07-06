package com.alant7_.util.command.tabcomplete;

import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Suggestion<T> {

    private T t;

    Suggestion(T t) {
        this.t = t;
    }

    public int hashCode() {
        if (this == ANY) {
            return 0;
        } else if (this == ONLINE_PLAYERS) {
            return 1;
        } else if (this == DYNAMIC) {
            return 2;
        }

        return super.hashCode();
    }

    public T getValue() {
        return t;
    }

    public static final Suggestion<?> ANY = new Suggestion<>(null);

    public static final Suggestion<?> ONLINE_PLAYERS = new Suggestion<>(null);

    public static final Suggestion<?> DYNAMIC = new Suggestion<>(null);

    public static Suggestion<String[]> ENUM(Class<?> enumclass) {
        return ENUM(enumclass, false);
    }

    public static Suggestion<String[]> ENUM_LOWERCASE(Class<?> enumclass) {
        return ENUM(enumclass, true);
    }

    private static Suggestion<String[]> ENUM(Class<?> enumclass, boolean lowercase) {
        Object[] o = enumclass.getEnumConstants();
        String[] s = new String[o.length];

        for (int i = 0; i < o.length; i++)
            s[i] = lowercase ? o[i].toString().toLowerCase() : o[i].toString();

        return create(s);
    }

    public static Suggestion<String> STRING(String str) {
        return create(str);
    }

    public static Suggestion<Integer> INT(int a) {
        return create(a);
    }

    public static Suggestion<String[]> STRING_ARRAY(String... arr) {
        return create(arr);
    }

    public static <T> Suggestion<String[]> STRING_ARRAY(Collection<T> list, Function<T, String> function) {
        return create(list.stream().map(function).toArray(String[]::new));
    }

    public static Suggestion<String[]> INT_ARRAY(int... arr) {
        String[] strarr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            strarr[i] = String.valueOf(arr[i]);
        }
        return create(strarr);
    }

    public static Suggestion<Double> DOUBLE(double d) {
        return create(d);
    }

    public static Suggestion<double[]> DOUBLE_ARRAY(double... arr) {
        return create(arr);
    }

    public static Suggestion<?> DYNAMIC(BiFunction<CommandSender, String[], List<String>> function) {
        return new DynamicSuggestion(function);
    }

    private static <T> Suggestion<T> create(T t) {
        return new Suggestion<>(t);
    }

}