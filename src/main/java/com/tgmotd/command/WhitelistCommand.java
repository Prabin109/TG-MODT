package com.tgmotd.command;

import com.tgmotd.core.TGMotd;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class WhitelistCommand implements SimpleCommand {

    private final TGMotd plugin;

    public WhitelistCommand(TGMotd plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        // Check if whitelisted or has permission
        boolean hasAccess = invocation.source().hasPermission("tgmotd.admin");
        
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            if (plugin.getWhitelistManager().isWhitelisted(player.getUsername())) {
                hasAccess = true;
            }
        }
        
        if (!hasAccess) {
            invocation.source().sendMessage(Component.text("No permission!", NamedTextColor.RED));
            return;
        }

        String[] args = invocation.arguments();

        if (args.length == 0) {
            invocation.source().sendMessage(Component.text("Usage:", NamedTextColor.YELLOW));
            invocation.source().sendMessage(Component.text("/whitelist add <player>", NamedTextColor.GRAY));
            invocation.source().sendMessage(Component.text("/whitelist remove <player>", NamedTextColor.GRAY));
            invocation.source().sendMessage(Component.text("/whitelist list", NamedTextColor.GRAY));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "add" -> {
                if (args.length < 2) {
                    invocation.source().sendMessage(Component.text("Usage: /whitelist add <player>", NamedTextColor.RED));
                    return;
                }
                String player = args[1];
                if (plugin.getWhitelistManager().addPlayer(player)) {
                    invocation.source().sendMessage(Component.text("Added " + player + " to whitelist!", NamedTextColor.GREEN));
                } else {
                    invocation.source().sendMessage(Component.text(player + " is already whitelisted!", NamedTextColor.RED));
                }
            }
            case "remove" -> {
                if (args.length < 2) {
                    invocation.source().sendMessage(Component.text("Usage: /whitelist remove <player>", NamedTextColor.RED));
                    return;
                }
                String player = args[1];
                if (plugin.getWhitelistManager().removePlayer(player)) {
                    invocation.source().sendMessage(Component.text("Removed " + player + " from whitelist!", NamedTextColor.GREEN));
                } else {
                    invocation.source().sendMessage(Component.text(player + " is not whitelisted!", NamedTextColor.RED));
                }
            }
            case "list" -> {
                List<String> players = plugin.getWhitelistManager().getWhitelistedPlayers();
                if (players.isEmpty()) {
                    invocation.source().sendMessage(Component.text("No whitelisted players.", NamedTextColor.GRAY));
                } else {
                    invocation.source().sendMessage(Component.text("Whitelisted players:", NamedTextColor.YELLOW));
                    for (String p : players) {
                        invocation.source().sendMessage(Component.text("- " + p, NamedTextColor.GRAY));
                    }
                }
            }
            default -> invocation.source().sendMessage(Component.text("Unknown subcommand!", NamedTextColor.RED));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if (invocation.source().hasPermission("tgmotd.admin")) {
            return true;
        }
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            return plugin.getWhitelistManager().isWhitelisted(player.getUsername());
        }
        return false;
    }
}
