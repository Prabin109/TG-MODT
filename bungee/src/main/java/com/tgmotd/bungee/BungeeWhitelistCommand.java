package com.tgmotd.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BungeeWhitelistCommand extends Command {

    private final BungeePlugin plugin;

    public BungeeWhitelistCommand(BungeePlugin plugin) {
        super("whitelist");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("tgmotd.whitelist")) {
            sender.sendMessage(new TextComponent("§cNo permission!"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(new TextComponent("§cUsage: /whitelist <add|remove> <player>"));
            return;
        }

        String action = args[0];
        String player = args[1];

        if (action.equalsIgnoreCase("add")) {
            plugin.getWhitelistManager().addPlayer(player);
            sender.sendMessage(new TextComponent("§aAdded " + player + " to whitelist!"));
        } else if (action.equalsIgnoreCase("remove")) {
            plugin.getWhitelistManager().removePlayer(player);
            sender.sendMessage(new TextComponent("§aRemoved " + player + " from whitelist!"));
        }
    }
}
