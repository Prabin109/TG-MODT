package com.tgmotd.velocity;

import com.google.inject.Inject;
import com.tgmotd.config.ConfigManager;
import com.tgmotd.config.WhitelistManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "tgmotd",
    name = "TG-MOTD",
    version = "1.0.1",
    description = "Advanced MOTD plugin",
    authors = {"Techinpoint Gamerz [TG]"}
)
public class VelocityPlugin {

    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataFolder;
    private ConfigManager configManager;
    private WhitelistManager whitelistManager;

    @Inject
    public VelocityPlugin(ProxyServer proxy, Logger logger, @DataDirectory Path dataFolder) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        configManager = new ConfigManager(dataFolder, logger);
        configManager.loadConfig();
        
        whitelistManager = new WhitelistManager(dataFolder, logger);
        whitelistManager.load();

        proxy.getEventManager().register(this, new VelocityPingHandler(this));
        proxy.getEventManager().register(this, new VelocityMaintenanceHandler(this));
        proxy.getCommandManager().register("tgmotd", new VelocityMotdCommand(this));
        proxy.getCommandManager().register("whitelist", new VelocityWhitelistCommand(this));
        
        logger.info("TG-MOTD enabled on Velocity!");
    }

    public void reload() {
        configManager.loadConfig();
        whitelistManager.load();
    }

    public Path getDataFolder() { return dataFolder; }
    public ProxyServer getProxy() { return proxy; }
    public Logger getLogger() { return logger; }
    public ConfigManager getConfigManager() { return configManager; }
    public WhitelistManager getWhitelistManager() { return whitelistManager; }
}
