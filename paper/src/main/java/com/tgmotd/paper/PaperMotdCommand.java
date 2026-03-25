package com.tgmotd.paper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PaperMotdCommand implements CommandExecutor {

    private final PaperPlugin plugin;

    public PaperMotdCommand(PaperPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tgmotd.admin")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage("§aTG-MOTD reloaded!");
        }
        return true;
    }
}
