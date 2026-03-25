package com.tgmotd.velocity;

import com.tgmotd.config.ConfigManager;
import com.tgmotd.util.TextFormatter;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class VelocityPingHandler {

    private final VelocityPlugin plugin;
    private Favicon favicon;
    private long lastUpdate;

    public VelocityPingHandler(VelocityPlugin plugin) {
        this.plugin = plugin;
        loadFavicon();
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > plugin.getConfigManager().getUpdateInterval()) {
            loadFavicon();
        }

        ConfigManager config = plugin.getConfigManager();
        int online = plugin.getProxy().getPlayerCount();
        int max = config.getMaxPlayers();

        online = online + config.getFakePlayersAdd() + (online * config.getFakePlayersPercent() / 100);

        List<String> motdLines = config.isMaintenanceEnabled() ? config.getMaintenanceMotd() : config.getMotdLines();
        List<String> hoverLines = config.isMaintenanceEnabled() ? config.getMaintenanceHover() : config.getHoverInfo();

        if (config.isMaintenanceEnabled() && config.overridePlayerCount()) {
            online = config.getMaintenanceOnline();
            max = config.getMaintenanceMax();
        }

        String motd = String.join("\n", motdLines);
        motd = TextFormatter.replacePlaceholders(motd, online, max);

        ServerPing.Builder builder = event.getPing().asBuilder()
            .description(TextFormatter.format(motd, config.getFormatter()))
            .maximumPlayers(max)
            .onlinePlayers(online);

        if (!hoverLines.isEmpty()) {
            ServerPing.SamplePlayer[] samples = new ServerPing.SamplePlayer[hoverLines.size()];
            for (int i = 0; i < hoverLines.size(); i++) {
                String line = TextFormatter.replacePlaceholders(hoverLines.get(i), online, max);
                samples[i] = new ServerPing.SamplePlayer(line, java.util.UUID.randomUUID());
            }
            builder.samplePlayers(samples);
        }

        if (favicon != null) {
            builder.favicon(favicon);
        }

        event.setPing(builder.build());
    }

    private void loadFavicon() {
        lastUpdate = System.currentTimeMillis();
        try {
            File iconFile = new File(plugin.getDataFolder().toFile(), plugin.getConfigManager().getIcon());
            if (iconFile.exists()) {
                favicon = Favicon.create(Path.of(iconFile.getAbsolutePath()));
            }
        } catch (Exception e) {
            favicon = null;
        }
    }
}
