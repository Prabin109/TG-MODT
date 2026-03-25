package com.tgmotd.velocity;

import com.tgmotd.util.TextFormatter;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;

public class VelocityMaintenanceHandler {

    private final VelocityPlugin plugin;

    public VelocityMaintenanceHandler(VelocityPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        if (!plugin.getConfigManager().isMaintenanceEnabled() || 
            !plugin.getConfigManager().shouldKickOnJoin()) {
            return;
        }

        if (plugin.getWhitelistManager().isWhitelisted(event.getPlayer().getUsername())) {
            return;
        }

        String kickMsg = plugin.getConfigManager().getKickMessage();
        event.setResult(ResultedEvent.ComponentResult.denied(
            TextFormatter.format(kickMsg, plugin.getConfigManager().getFormatter())));
    }
}
