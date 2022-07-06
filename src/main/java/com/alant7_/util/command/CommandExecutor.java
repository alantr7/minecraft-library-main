package com.alant7_.util.command;

import com.alant7_.util.BukkitPlugin;
import com.alant7_.util.command.tabcomplete.Suggestion;
import com.alant7_.util.command.tabcomplete.TabCompleter;
import com.alant7_.util.reflections.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class CommandExecutor implements org.bukkit.command.CommandExecutor, org.bukkit.command.TabCompleter {

    private final Map<String, TabCompleter> tabCompleterMap = new HashMap<>();

    private final Map<String, Pair<Method, com.alant7_.util.command.Command>> methodsNoParams = new HashMap<>();

    private final Map<String, Map<Method, com.alant7_.util.command.Command>> methods = new HashMap<>();

    private final Map<Method, Object> methodParents = new HashMap<>();

    private final List<CommandExpansion> expansions = new ArrayList<>();

    public CommandExecutor() {
        init();
    }

    protected abstract void init();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, String s, String[] args) {

        String base = cmd.getName().toLowerCase();

        Map<Method, com.alant7_.util.command.Command> methods = this.methods.get(base);

        if (methods == null)
            return false;

        Method methodNoParams = methodsNoParams.get(base).getKey();

        com.alant7_.util.command.Command commandNoParams = methodsNoParams.get(base).getValue();

        if (args.length == 0) {

            if (methodNoParams != null) {
                if (sender instanceof Player && commandNoParams.permission().length() > 0 && !sender.hasPermission(commandNoParams.permission())) {
                    sender.sendMessage(commandNoParams.permissionMessage());
                    return false;
                }

                invoke(methodNoParams, new ExecutedCommand(sender, base, new String[0], false, false));
            }

            return true;

        }

        final Pair<Method, com.alant7_.util.command.Command> match = new Pair<>();

        AtomicInteger maxArgsMatch = new AtomicInteger(0);

        methods.forEach(((method, command) -> {

            String[] params = command.params();

            int min = Math.min(params.length, args.length);

            for (int i = 0; i < min; i++) {

                if (!params[i].equalsIgnoreCase(args[i]) && !params[i].equals(com.alant7_.util.command.Command.ANY)) {
                    return;
                }

            }

            if (match.getKey() == null || match.getValue() == null || min > maxArgsMatch.get()) {
                match.setKey(method);
                match.setValue(command);
                maxArgsMatch.set(min);
            } else {

                if (params.length == args.length) {
                    if (match.getValue().params().length == params.length) {

                        for (int i = 0; i < match.getValue().params().length; i++) {
                            if (match.getValue().params()[i].equals(com.alant7_.util.command.Command.ANY) && !params[i].equals(com.alant7_.util.command.Command.ANY)) {
                                match.setKey(method);
                                match.setValue(command);
                                maxArgsMatch.set(min);
                                break;
                            }
                        }

                    } else {
                        match.setKey(method);
                        match.setValue(command);
                        maxArgsMatch.set(min);
                    }
                }

            }
        }));

        com.alant7_.util.command.Command command;

        Method method;

        boolean tooFew = false;

        boolean tooMany = true;

        // Finds an appropriate method for the command
        if (match.getKey() != null) {
            method = match.getKey();
            command = match.getValue();
            tooFew = args.length < match.getValue().params().length;
            tooMany = args.length > match.getValue().params().length;
        } else if (methodNoParams != null) {
            method = methodNoParams;
            command = commandNoParams;
        } else return false;

        // Checks if command requires a player
        if (command.requirePlayer() && !(sender instanceof Player)) {
            sender.sendMessage(command.requirePlayerMessage());
            return false;
        }

        // Checks if command requires a specific permission
        if (sender instanceof Player && command.permission().length() > 0 && !sender.hasPermission(command.permission())) {
            sender.sendMessage(command.permissionMessage());
            return false;
        }

        // Checks if command requires OP
        if (sender instanceof Player && command.op() && !sender.isOp()) {
            sender.sendMessage(command.permissionMessage());
            return false;
        }

        // Calls the most appropriate method for the command
        invoke(method, new ExecutedCommand(sender, base, args, tooFew, tooMany));

        return true;

    }

    private void invoke(Method m, ExecutedCommand c) {
        try {
            m.setAccessible(true);
            m.invoke(methodParents.get(m), c);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public TabCompleter tabCompleter(String base) {
        return tabCompleterMap.computeIfAbsent(base, k -> new TabCompleter());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {

        String base = cmd.getName().toLowerCase();

        TabCompleter tabCompleter = tabCompleterMap.get(base);

        boolean isPlayer = sender instanceof Player;

        if (tabCompleter == null) {
            return Collections.emptyList();
        }

        for (int i = 0; i < args.length - 1; i++) {
            String arg = args[i];
            if (tabCompleter == null) {
                return Collections.emptyList();
            }
            if (tabCompleter.isAny()) {
                tabCompleter = tabCompleter.get(Suggestion.ANY);
            } else if (tabCompleter.hasKey(Suggestion.ONLINE_PLAYERS)) {
                tabCompleter = tabCompleter.get(Suggestion.ONLINE_PLAYERS);
            } else if (tabCompleter.isDynamic()) {
                tabCompleter = tabCompleter.get(Suggestion.DYNAMIC);
            } else if (tabCompleter.isValid(arg)) {
                tabCompleter = tabCompleter.get(arg);
            } else {
                return Collections.emptyList();
            }

            if (tabCompleter != null && isPlayer && tabCompleter.hasRequiredPermission() && !sender.hasPermission(tabCompleter.getRequiredPermission())) {
                return Collections.emptyList();
            }
        }

        return tabCompleter != null ? tabCompleter.process(sender, args[args.length - 1], args) : Collections.emptyList();

    }

    public void register(BukkitPlugin plugin) {

        List<Object> objects = new ArrayList<>(expansions);

        objects.add(this);

        objects.forEach(object -> Arrays.stream(object.getClass().getDeclaredMethods()).forEach(method -> {
            com.alant7_.util.command.Command a = method.getDeclaredAnnotation(com.alant7_.util.command.Command.class);
            if (a != null) {
                method.setAccessible(true);
                String base = a.base();
                if (a.params().length == 0) {
                    methodsNoParams.put(base, new Pair<>(method, a));
                }

                Map<Method, com.alant7_.util.command.Command> map = methods.computeIfAbsent(base, k -> new HashMap<>());
                map.put(method, a);

                methodParents.put(method, object);
                PluginCommand cmd = plugin.getCommand(base);
                if (cmd != null) {
                    cmd.setExecutor(this);
                    cmd.setTabCompleter(this);
                }
            }
        }));
    }

    public void registerExpansion(CommandExpansion expansion) {
        if (expansions.contains(expansion))
            return;

        expansion.init(this);
        expansions.add(expansion);
    }

}
