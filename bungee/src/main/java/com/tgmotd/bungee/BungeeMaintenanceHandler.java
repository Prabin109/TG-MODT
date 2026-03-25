package com.tgmotd.bungee;

import com.tgmotd.util.TextFormatter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeMaintenanceHandler implements Listener {

    private final BungeePlugin plugin;

    public BungeeMaintenanceHandler(BungeePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        if (!plugin.getConfigManager().isMaintenanceEnabled() || 
            !plugin.getConfigManager().shouldKickOnJoin()) {
            return;
        }

        if (plugin.getWhitelistManager().isWhitelisted(event.getConnection().getName())) {
            return;
        }

        String kickMsg = plugin.getConfigManager().getKickMessage();
        event.setCancelled(true);
        event.setCancelReason(new TextComponent(kickMsg));
    }
}
