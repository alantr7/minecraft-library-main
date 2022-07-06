package com.alant7_.util.command.tabcomplete;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TabCompleterArray extends TabCompleter {

    private final List<TabCompleter> tabCompleters = new ArrayList<>();

    public void addTabCompleter(TabCompleter completer) {
        tabCompleters.add(completer);
    }

    @Override
    public TabCompleter get(String key) {
        return super.get(key);
    }

    @Override
    public TabCompleter get(Suggestion<?> key) {
        return super.get(key);
    }

    @Override
    public TabCompleter add(String key) {
        tabCompleters.forEach(tc -> tc.add(key));
        return this;
    }

    @Override
    public TabCompleter addAndGet(Suggestion<?> key) {
        TabCompleterArray array = new TabCompleterArray();
        tabCompleters.forEach(tc -> {
            array.addTabCompleter(tc.addAndGet(key));
        });
        super.addAndGet(key);
        return array;
    }

    @Override
    public TabCompleter add(Suggestion<?> key, Consumer<TabCompleter> operator) {
        tabCompleters.forEach(tc -> tc.add(key, operator));
        return super.add(key, operator);
    }

    @Override
    public TabCompleter addBranch(Suggestion<?>... objects) {
        tabCompleters.forEach(tc -> tc.addBranch(objects));
        return super.addBranch(objects);
    }

    @Override
    public TabCompleter setRequiredPermission(String permission) {
        tabCompleters.forEach(tc -> tc.setRequiredPermission(permission));
        super.setRequiredPermission(permission);

        return this;
    }

}
