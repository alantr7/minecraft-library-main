package com.alant7_.util;

import com.alant7_.util.config.Configuration;
import com.alant7_.util.reflections.Pair;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Language extends Configuration {

    private static HashMap<AlanJavaPlugin, Language> instances = new HashMap<>();

    private Language(AlanJavaPlugin plugin, String path) {
        super(plugin, path);
    }

    public static void init(AlanJavaPlugin plugin, String path) {
        instances.put(plugin, new Language(plugin, path));
        instances.get(plugin).load();
    }

    public static String get(AlanJavaPlugin plugin, String message) {
        String msg = instances.get(plugin).getConfig().getString(message);
        return msg != null ? Formatter.formatColors(msg) : "";
    }

    public static List<String> getList(AlanJavaPlugin plugin, String message, String[] replace, Object[] replaceWith) {
        List<String> list = instances.get(plugin).getConfig().getStringList(message);
        List<String> r = new ArrayList<>();

        for (String line : list) {
            String formatted = Formatter.formatColors(line);
            for (int i = 0; i < replace.length; i++)
                formatted = formatted.replace(replace[i], replaceWith[i].toString());

            r.add(formatted);
        }

        return r;
    }

    @SafeVarargs
    public static List<String> getList(AlanJavaPlugin plugin, String message, Pair<String, Object>... pairs) {
        List<String> list = instances.get(plugin).getConfig().getStringList(message);
        List<String> r = new ArrayList<>();

        for (String line : list) {
            String formatted = Formatter.formatColors(line);
            for (int i = 0; i < pairs.length; i++)
                formatted = formatted.replace(pairs[i].getKey(), pairs[i].getValue().toString());

            r.add(formatted);
        }

        return r;
    }

    public static List<String> getList(AlanJavaPlugin plugin, String message) {
        return getList(plugin, message, new String[0], new String[0]);
    }

    @Override
    public void onConfigReady(FileConfiguration config) {

    }

}
