package com.tgmotd.paper;

import com.tgmotd.util.TextFormatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PaperMaintenanceHandler implements Listener {

    private final PaperPlugin plugin;

    public PaperMaintenanceHandler(PaperPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!plugin.getConfigManager().isMaintenanceEnabled() || 
            !plugin.getConfigManager().shouldKickOnJoin()) {
            return;
        }

        if (plugin.getWhitelistManager().isWhitelisted(event.getPlayer().getName())) {
            return;
        }

        String kickMsg = plugin.getConfigManager().getKickMessage();
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, 
            TextFormatter.format(kickMsg, plugin.getConfigManager().getFormatter()));
    }
}
