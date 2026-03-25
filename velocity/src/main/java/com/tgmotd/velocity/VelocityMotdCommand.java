package com.tgmotd.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class VelocityMotdCommand implements SimpleCommand {

    private final VelocityPlugin plugin;

    public VelocityMotdCommand(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!invocation.source().hasPermission("tgmotd.admin")) {
            invocation.source().sendMessage(Component.text("No permission!", NamedTextColor.RED));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length == 0 || args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            invocation.source().sendMessage(Component.text("TG-MOTD reloaded!", NamedTextColor.GREEN));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("tgmotd.admin");
    }
}
