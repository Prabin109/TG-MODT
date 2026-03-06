package com.tgmotd.handler;

import com.tgmotd.core.TGMotd;
import com.tgmotd.util.TextFormatter;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import net.kyori.adventure.text.Component;

public class MaintenanceHandler {

    private final TGMotd plugin;

    public MaintenanceHandler(TGMotd plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        if (!plugin.getConfigManager().isMaintenanceEnabled()) {
            return;
        }

        if (!plugin.getConfigManager().shouldKickOnJoin()) {
            return;
        }

        // Check whitelist
        String username = event.getPlayer().getUsername();
        
        if (plugin.getWhitelistManager().isWhitelisted(username)) {
            return; // Allow whitelisted players
        }

        String kickMsg = plugin.getConfigManager().getKickMessage();
        String formatter = plugin.getConfigManager().getFormatter();
        Component kickComponent = TextFormatter.format(kickMsg, formatter);
        
        event.setResult(ResultedEvent.ComponentResult.denied(kickComponent));
    }
}
