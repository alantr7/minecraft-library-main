package com.alant7_.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    static Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String formatColors(String text) {
        if (text == null)
            return "";
        text = ChatColor.translateAlternateColorCodes('&', text);

        String r = text;
        Matcher matcher = hexPattern.matcher(r);
        while (matcher.find()) {
            String hex = text.substring(matcher.start(), matcher.end());
            r = r.replace(hex, ChatColor.of(hex).toString());
        }

        return r;
    }

    public static String formatEnum(Enum<?> clazz) {

        String name = clazz.name().toLowerCase();

        String[] split = name.indexOf('_') != -1 ? name.split("_") : new String[] { name };

        name = "";

        for (int i = 0; i < split.length; i++) {
            String part = split[i];
            name += String.valueOf(part.charAt(0)).toUpperCase() + part.substring(1) + " ";
        }

        return name.trim();

    }

    public static String stripColor(String string) {
        return org.bukkit.ChatColor.stripColor(org.bukkit.ChatColor.translateAlternateColorCodes('&', string));
    }

}
