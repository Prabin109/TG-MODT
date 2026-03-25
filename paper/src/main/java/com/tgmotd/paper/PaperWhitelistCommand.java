package com.tgmotd.paper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PaperWhitelistCommand implements CommandExecutor {

    private final PaperPlugin plugin;

    public PaperWhitelistCommand(PaperPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tgmotd.whitelist")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /whitelist <add|remove> <player>");
            return true;
        }

        String action = args[0];
        String player = args[1];

        if (action.equalsIgnoreCase("add")) {
            plugin.getWhitelistManager().addPlayer(player);
            sender.sendMessage("§aAdded " + player + " to whitelist!");
        } else if (action.equalsIgnoreCase("remove")) {
            plugin.getWhitelistManager().removePlayer(player);
            sender.sendMessage("§aRemoved " + player + " from whitelist!");
        }
        return true;
    }
}
