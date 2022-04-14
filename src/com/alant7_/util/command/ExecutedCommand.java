package com.alant7_.util.command;

import com.alant7_.util.objects.Holder;
import org.bukkit.Bukkit;
import org.bukkit.Warning;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExecutedCommand {

    private final Player player;

    public ExecutedCommand(CommandSender sender, String base, String[] args, boolean fewArguments, boolean extraArguments) {
        this.sender = sender;
        this.base = base;
        this.args = args;
        this.fewArguments = fewArguments;
        this.extraArguments = extraArguments;
        this.player = sender instanceof Player ? (Player) sender : null;
    }

    public CommandSender sender() {
        return sender;
    }

    public void respond(String message) {
        sender.sendMessage(message);
    }

    @Deprecated
    public boolean hasPlayer() {
        return player != null;
    }

    public boolean isExecutorPlayer() {
        return player != null;
    }

    public boolean isExecutorConsole() {
        return player == null;
    }

    public Player player() {
        return player;
    }

    public String base() {
        return base;
    }

    public boolean hasPermission(String permission) {
        return player == null || player.hasPermission(permission);
    }

    public boolean hasFewArguments() {
        return fewArguments;
    }

    public boolean hasExtraArguments() {
        return extraArguments;
    }

    public boolean hasEnoughArguments() {
        return !hasFewArguments() && !hasExtraArguments();
    }

    public boolean is(int argument, String... compareToArr) {
        if (argument >= args.length)
            return false;

        for (String compareTo : compareToArr)
            if (args[argument].equalsIgnoreCase(compareTo))
                return true;

        return false;
    }

    public @NotNull Holder<Integer> getInt(int arg) {
        if (arg >= args.length || arg < 0)
            return new Holder<>();

        String argument = args[arg];
        if (argument.matches("(-|)[0-9]+"))
            return new Holder<>(Integer.parseInt(argument));

        return new Holder<>();
    }

    public Holder<Double> getDouble(int arg) {
        if (arg >= args.length || arg < 0)
            return new Holder<>();

        String argument = args[arg];
        if (argument.matches("(-|)([0-9]+|)(\\.|)[0-9]+"))
            return new Holder<>(Double.parseDouble(argument));

        return new Holder<>();
    }

    public <T extends Enum<T>> T getEnum(int arg, Class<T> clazz) {
        if (arg >= args.length || arg < 0)
            return null;

        String argument = args[arg].toUpperCase();
        try {
            return Enum.valueOf(clazz, argument);
        } catch (Exception e) {
            return null;
        }
    }

    public Player getPlayer(int arg) {
        if (arg >= args.length || arg < 0)
            return null;

        return Bukkit.getPlayer(args[arg]);
    }

    private final CommandSender sender;

    private final String base;

    public final String[] args;

    private final boolean fewArguments;

    private final boolean extraArguments;

}
