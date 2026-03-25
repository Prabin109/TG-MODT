package com.tgmotd.paper;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.tgmotd.util.TextFormatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;

public class PaperPingHandler implements Listener {

    private final PaperPlugin plugin;
    private CachedServerIcon icon;
    private long lastUpdate;

    public PaperPingHandler(PaperPlugin plugin) {
        this.plugin = plugin;
        loadIcon();
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > plugin.getConfigManager().getUpdateInterval()) {
            loadIcon();
        }

        int online = plugin.getServer().getOnlinePlayers().size();
        int max = plugin.getConfigManager().getMaxPlayers();

        online = online + plugin.getConfigManager().getFakePlayersAdd() + 
                 (online * plugin.getConfigManager().getFakePlayersPercent() / 100);

        List<String> motdLines = plugin.getConfigManager().isMaintenanceEnabled() 
            ? plugin.getConfigManager().getMaintenanceMotd() 
            : plugin.getConfigManager().getMotdLines();

        String motd = String.join("\n", motdLines);
        motd = TextFormatter.replacePlaceholders(motd, online, max);
        event.motd(TextFormatter.format(motd, plugin.getConfigManager().getFormatter()));
        
        event.setMaxPlayers(max);
        event.setNumPlayers(online);

        if (icon != null) {
            event.setServerIcon(icon);
        }
    }

    private void loadIcon() {
        lastUpdate = System.currentTimeMillis();
        try {
            File iconFile = new File(plugin.getDataFolder(), plugin.getConfigManager().getIcon());
            if (iconFile.exists()) {
                icon = plugin.getServer().loadServerIcon(ImageIO.read(iconFile));
            }
        } catch (Exception e) {
            icon = null;
        }
    }
}
