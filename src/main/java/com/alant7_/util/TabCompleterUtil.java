package com.alant7_.util;

import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class TabCompleterUtil implements TabCompleter {

    public boolean shouldGetSuggested(String arg, String targetArg) {

        if (arg.length() == 0)
            return true;

        if (arg.charAt(0) != arg.charAt(0))
            return false;

        if (arg.length() > targetArg.length())
            return false;

        for (int i = 0; i < arg.length(); i++) {
            if (arg.charAt(i) != targetArg.charAt(i)) {
                return false;
            }
        }

        return true;

    }

    // Suggest argument that match argument that sender began writing
    public void suggest(List<String> arr, String arg, String... args) {
        for (String arg1 : args)
            if (shouldGetSuggested(arg, arg1))
                arr.add(arg1);
    }

}
