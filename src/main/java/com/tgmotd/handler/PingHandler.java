package com.tgmotd.handler;

import com.tgmotd.core.TGMotd;
import com.tgmotd.util.TextFormatter;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PingHandler {

    private final TGMotd plugin;
    private String cachedFavicon;
    private long lastUpdate;

    public PingHandler(TGMotd plugin) {
        this.plugin = plugin;
        this.refresh();
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) throws IOException {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > plugin.getConfigManager().getUpdateInterval()) {
            refresh();
        }

        ServerPing.Builder builder = event.getPing().asBuilder();
        
        int online = plugin.getProxy().getPlayerCount();
        int max = plugin.getConfigManager().getMaxPlayers();

        // Apply fake players
        int fakeAdd = plugin.getConfigManager().getFakePlayersAdd();
        int fakePercent = plugin.getConfigManager().getFakePlayersPercent();
        online = online + fakeAdd + (online * fakePercent / 100);

        List<String> motdLines;
        List<String> hoverLines;

        if (plugin.getConfigManager().isMaintenanceEnabled()) {
            motdLines = plugin.getConfigManager().getMaintenanceMotd();
            hoverLines = plugin.getConfigManager().getMaintenanceHover();
            
            if (plugin.getConfigManager().overridePlayerCount()) {
                online = plugin.getConfigManager().getMaintenanceOnline();
                max = plugin.getConfigManager().getMaintenanceMax();
            }
        } else {
            motdLines = plugin.getConfigManager().getMotdLines();
            hoverLines = plugin.getConfigManager().getHoverInfo();
        }

        // Build MOTD
        String motd = String.join("\n", motdLines);
        motd = TextFormatter.replacePlaceholders(motd, online, max);
        String formatter = plugin.getConfigManager().getFormatter();
        Component description = TextFormatter.format(motd, formatter);
        
        builder.description(description);
        builder.maximumPlayers(max);
        builder.onlinePlayers(online);

        // Add hover info
        if (!hoverLines.isEmpty()) {
            ServerPing.SamplePlayer[] samples = new ServerPing.SamplePlayer[hoverLines.size()];
            for (int i = 0; i < hoverLines.size(); i++) {
                String line = TextFormatter.replacePlaceholders(hoverLines.get(i), online, max);
                samples[i] = new ServerPing.SamplePlayer(line, java.util.UUID.randomUUID());
            }
            builder.samplePlayers(samples);
        }

        if (cachedFavicon != null) {
            builder.favicon(Favicon.create(Path.of(cachedFavicon)));
        }

        event.setPing(builder.build());
    }

    public void refresh() {
        lastUpdate = System.currentTimeMillis();
        
        String iconPath = plugin.getConfigManager().getIcon();
        File dataDir = plugin.getProxy().getPluginManager()
            .getPlugin("tgmotd")
            .flatMap(p -> p.getDescription().getSource())
            .map(path -> path.getParent().toFile())
            .orElse(new File("."));
        
        File iconFile = new File(dataDir, iconPath);
        
        try {
            cachedFavicon = TextFormatter.loadFavicon(iconFile);
        } catch (IOException e) {
            cachedFavicon = null;
        }
    }
}
