package com.tgmotd.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BungeeMotdCommand extends Command {

    private final BungeePlugin plugin;

    public BungeeMotdCommand(BungeePlugin plugin) {
        super("tgmotd");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("tgmotd.admin")) {
            sender.sendMessage(new TextComponent("§cNo permission!"));
            return;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage(new TextComponent("§aTG-MOTD reloaded!"));
        }
    }
}
