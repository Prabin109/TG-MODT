package com.tgmotd.bungee;

import com.tgmotd.config.ConfigManager;
import com.tgmotd.config.WhitelistManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class BungeePlugin extends Plugin {

    private ConfigManager configManager;
    private WhitelistManager whitelistManager;

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        
        configManager = new ConfigManager(getDataFolder().toPath(), 
            org.slf4j.LoggerFactory.getLogger("TG-MOTD"));
        configManager.loadConfig();
        
        whitelistManager = new WhitelistManager(getDataFolder().toPath(), 
            org.slf4j.LoggerFactory.getLogger("TG-MOTD"));
        whitelistManager.load();

        getProxy().getPluginManager().registerListener(this, new BungeePingHandler(this));
        getProxy().getPluginManager().registerListener(this, new BungeeMaintenanceHandler(this));
        getProxy().getPluginManager().registerCommand(this, new BungeeMotdCommand(this));
        getProxy().getPluginManager().registerCommand(this, new BungeeWhitelistCommand(this));
        
        logger.info("TG-MOTD enabled on BungeeCord!");
    }

    public void reload() {
        configManager.loadConfig();
        whitelistManager.load();
    }

    public ConfigManager getConfigManager() { return configManager; }
    public WhitelistManager getWhitelistManager() { return whitelistManager; }
}
