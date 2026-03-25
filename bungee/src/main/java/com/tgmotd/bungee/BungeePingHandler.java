package com.tgmotd.bungee;

import com.tgmotd.util.TextFormatter;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;

public class BungeePingHandler implements Listener {

    private final BungeePlugin plugin;
    private Favicon favicon;
    private long lastUpdate;

    public BungeePingHandler(BungeePlugin plugin) {
        this.plugin = plugin;
        loadFavicon();
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > plugin.getConfigManager().getUpdateInterval()) {
            loadFavicon();
        }

        ServerPing ping = event.getResponse();
        int online = plugin.getProxy().getOnlineCount();
        int max = plugin.getConfigManager().getMaxPlayers();

        online = online + plugin.getConfigManager().getFakePlayersAdd() + 
                 (online * plugin.getConfigManager().getFakePlayersPercent() / 100);

        List<String> motdLines = plugin.getConfigManager().isMaintenanceEnabled() 
            ? plugin.getConfigManager().getMaintenanceMotd() 
            : plugin.getConfigManager().getMotdLines();

        String motd = String.join("\n", motdLines);
        motd = TextFormatter.replacePlaceholders(motd, online, max);

        ServerPing.Players players = new ServerPing.Players(max, online, null);
        event.setResponse(new ServerPing(ping.getVersion(), players, 
            new TextComponent(motd), favicon));
    }

    private void loadFavicon() {
        lastUpdate = System.currentTimeMillis();
        try {
            File iconFile = new File(plugin.getDataFolder(), plugin.getConfigManager().getIcon());
            if (iconFile.exists()) {
                favicon = Favicon.create(ImageIO.read(iconFile));
            }
        } catch (Exception e) {
            favicon = null;
        }
    }
}
