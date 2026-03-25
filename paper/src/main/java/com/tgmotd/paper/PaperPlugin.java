package com.tgmotd.paper;

import com.tgmotd.config.ConfigManager;
import com.tgmotd.config.WhitelistManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaperPlugin extends JavaPlugin {

    private static final Logger logger = LoggerFactory.getLogger("TG-MOTD");
    private ConfigManager configManager;
    private WhitelistManager whitelistManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(getDataFolder().toPath(), logger);
        configManager.loadConfig();
        
        whitelistManager = new WhitelistManager(getDataFolder().toPath(), logger);
        whitelistManager.load();

        getServer().getPluginManager().registerEvents(new PaperPingHandler(this), this);
        getServer().getPluginManager().registerEvents(new PaperMaintenanceHandler(this), this);
        getCommand("tgmotd").setExecutor(new PaperMotdCommand(this));
        getCommand("whitelist").setExecutor(new PaperWhitelistCommand(this));
        
        logger.info("TG-MOTD enabled on Paper/Spigot!");
    }

    public void reload() {
        configManager.loadConfig();
        whitelistManager.load();
    }

    public ConfigManager getConfigManager() { return configManager; }
    public WhitelistManager getWhitelistManager() { return whitelistManager; }
}
