package com.tgmotd.command;

import com.tgmotd.core.TGMotd;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MotdCommand implements SimpleCommand {

    private final TGMotd plugin;

    public MotdCommand(TGMotd plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        // Check if whitelisted or has permission
        boolean hasAccess = false;
        
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            if (plugin.getWhitelistManager().isWhitelisted(player.getUsername())) {
                hasAccess = true;
            }
        }
        
        if (!hasAccess && !invocation.source().hasPermission("tgmotd.admin")) {
            invocation.source().sendMessage(Component.text("No permission!", NamedTextColor.RED));
            return;
        }

        if (invocation.arguments().length == 0) {
            invocation.source().sendMessage(Component.text("Usage: /tgmotd reload", NamedTextColor.YELLOW));
            return;
        }

        if (invocation.arguments()[0].equalsIgnoreCase("reload")) {
            plugin.reloadPlugin();
            invocation.source().sendMessage(Component.text("Configuration reloaded!", NamedTextColor.GREEN));
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
