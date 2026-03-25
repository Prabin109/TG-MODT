package com.tgmotd.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class VelocityWhitelistCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public VelocityWhitelistCommand(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!invocation.source().hasPermission("tgmotd.whitelist")) {
            invocation.source().sendMessage(Component.text("No permission!", NamedTextColor.RED));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 2) {
            invocation.source().sendMessage(Component.text("Usage: /whitelist <add|remove> <player>", NamedTextColor.RED));
            return;
        }

        String action = args[0];
        String player = args[1];

        if (action.equalsIgnoreCase("add")) {
            plugin.getWhitelistManager().addPlayer(player);
            invocation.source().sendMessage(Component.text("Added " + player + " to whitelist!", NamedTextColor.GREEN));
        } else if (action.equalsIgnoreCase("remove")) {
            plugin.getWhitelistManager().removePlayer(player);
            invocation.source().sendMessage(Component.text("Removed " + player + " from whitelist!", NamedTextColor.GREEN));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("tgmotd.whitelist");
    }
}
