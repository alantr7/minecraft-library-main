package com.alant7_.util;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class CommandUtil extends TabCompleterUtil implements CommandExecutor, TabCompleter {

    public boolean isPermitted(CommandSender sender, String permission) {
        return !(sender instanceof Player) || sender.hasPermission(permission);
    }

    public boolean requirePlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can be run by player only.");
            return true;
        }

        return false;
    }

    public static void register(BukkitPlugin plugin, CommandUtil util, String... commands) {
        for (String cmd : commands) {
            plugin.getCommand(cmd).setExecutor(util);
            plugin.getCommand(cmd).setTabCompleter(util);
        }
    }

}
