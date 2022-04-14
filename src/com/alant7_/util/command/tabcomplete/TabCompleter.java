package com.alant7_.util.command.tabcomplete;

import com.google.common.annotations.Beta;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class TabCompleter {

    final Map<Object, TabCompleter> completerMap = new HashMap<>();

    private String permission;

    public TabCompleter get(String key) {
        return completerMap.get(key);
    }

    public TabCompleter get(Suggestion<?> key) {

        if (key == Suggestion.ANY) {
            return completerMap.get(Suggestion.ANY);
        } else if (key == Suggestion.ONLINE_PLAYERS) {
            return completerMap.get(Suggestion.ONLINE_PLAYERS);
        } else if (key == Suggestion.DYNAMIC || key instanceof DynamicSuggestion) {
            for (Object obj : completerMap.keySet())
                if (obj instanceof DynamicSuggestion)
                    return completerMap.get(obj);
        }

        return completerMap.getOrDefault(String.valueOf(key.getValue()), null);

    }

    public TabCompleter add(String key) {

        TabCompleter tabCompleter = new TabCompleter();

        completerMap.put(key, tabCompleter);

        return this;
    }

    public TabCompleter add(Suggestion<?> key) {

        addAndGet(key);

        return this;

    }

    public TabCompleter addAndGet(Suggestion<?> key) {

        TabCompleter tabCompleter = new TabCompleter();

        if (key == Suggestion.ANY) {
            completerMap.put(Suggestion.ANY, tabCompleter);
        } else if (key == Suggestion.ONLINE_PLAYERS) {
            completerMap.put(Suggestion.ONLINE_PLAYERS, tabCompleter);
        } else if (key instanceof DynamicSuggestion) {
            completerMap.put(key, tabCompleter);
        } else {
            if (key.getValue().getClass().isArray()) {
                var tabCompleterArray = new TabCompleterArray();
                for (int i = 0; i < Array.getLength(key.getValue()); i++) {
                    var completer = new TabCompleter();
                    completerMap.put(Array.get(key.getValue(), i).toString(), completer);

                    tabCompleterArray.addTabCompleter(completer);
                }
                return tabCompleterArray;
            } else {
                completerMap.put(String.valueOf(key.getValue()), tabCompleter);
            }
        }

        return tabCompleter;

    }

    public TabCompleter addAndGet(String key) {

        TabCompleter tabCompleter = new TabCompleter();

        completerMap.put(key, tabCompleter);

        return tabCompleter;

    }

    public TabCompleter add(Suggestion<?> key, Consumer<TabCompleter> operator) {

        TabCompleter tabCompleter = new TabCompleter();

        if (key == Suggestion.ANY) {
            completerMap.put(Suggestion.ANY, tabCompleter);
        } else if (key == Suggestion.ONLINE_PLAYERS) {
            completerMap.put(Suggestion.ONLINE_PLAYERS, tabCompleter);
        } else if (key instanceof DynamicSuggestion) {
            completerMap.put(key, tabCompleter);
        } else {
            if (key.getValue().getClass().isArray()) {
                for (int i = 0; i < Array.getLength(key.getValue()); i++) {
                    TabCompleter tabCompleter1 = new TabCompleter();
                    operator.accept(tabCompleter1);
                    completerMap.put(Array.get(key.getValue(), i).toString(), tabCompleter1);
                }
                tabCompleter = null;
            } else {
                completerMap.put(String.valueOf(key.getValue()), tabCompleter);
            }
        }

        if (tabCompleter != null) {
            operator.accept(tabCompleter);
        }

        return this;

    }

    public TabCompleter addBranch(Suggestion<?>... objects) {
        TabCompleter current = this;
        for (Suggestion<?> object : objects) {
            current = current.addAndGet(object);
        }
        return this;
    }

    public TabCompleter addBranch(String... objects) {
        TabCompleter current = this;
        for (String object : objects)
            current = current.addAndGet(Suggestion.STRING(object));
        return this;
    }

    public boolean isValid(String key) {
        return completerMap.containsKey(Suggestion.ANY) || completerMap.containsKey(Suggestion.ONLINE_PLAYERS) || completerMap.containsKey(key);
    }

    public boolean isAny() {
        return completerMap.containsKey(Suggestion.ANY);
    }

    public boolean isDynamic() {
        return get(Suggestion.DYNAMIC) != null;
    }

    public boolean hasKey(Object object) {
        return completerMap.containsKey(object);
    }

    public List<String> process(CommandSender sender, String string, String[] args) {
        List<String> r = new ArrayList<>();

        completerMap.forEach((key, completer) -> {

            if (key == Suggestion.ANY)
                return;

            if (completer.hasRequiredPermission() && !sender.hasPermission(completer.getRequiredPermission()))
                return;

            if (key == Suggestion.ONLINE_PLAYERS) {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (shouldGetSuggested(string, player.getName()))
                        r.add(player.getName());

                return;
            }

            if (key instanceof DynamicSuggestion) {
                ((DynamicSuggestion) key).getValue().apply(sender, args).forEach(suggestion -> {
                    if (shouldGetSuggested(string, suggestion)) {
                        r.add(suggestion);
                    }
                });
                return;
            }

            if (shouldGetSuggested(string, key.toString())) {
                r.add(key.toString());
            }

        });

        return r;
    }

    public TabCompleter setRequiredPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public String getRequiredPermission() {
        return permission;
    }

    public boolean hasRequiredPermission() {
        return permission != null && permission.length() > 0;
    }

    private boolean shouldGetSuggested(String arg, String targetArg) {

        arg = arg.toLowerCase();

        targetArg = targetArg.toLowerCase();

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
    private void suggest(List<String> arr, String arg, String... args) {
        for (String arg1 : args)
            if (shouldGetSuggested(arg, arg1))
                arr.add(arg1);
    }

}
