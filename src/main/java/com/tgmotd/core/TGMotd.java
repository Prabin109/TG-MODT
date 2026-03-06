package com.tgmotd.core;

import com.google.inject.Inject;
import com.tgmotd.command.MotdCommand;
import com.tgmotd.command.WhitelistCommand;
import com.tgmotd.config.WhitelistManager;
import com.tgmotd.config.ConfigManager;
import com.tgmotd.handler.MaintenanceHandler;
import com.tgmotd.handler.PingHandler;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "tgmotd",
    name = "TG-MOTD",
    version = "1.0.0",
    description = "Advanced MOTD plugin for Velocity",
    authors = {"Techinpoint Gamerz [TG]"}
)
public class TGMotd {

    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataFolder;
    private ConfigManager configManager;
    private WhitelistManager whitelistManager;
    private PingHandler pingHandler;
    private MaintenanceHandler maintenanceHandler;

    @Inject
    public TGMotd(ProxyServer proxy, Logger logger, @DataDirectory Path dataFolder) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        logger.info("Initializing TG-MOTD plugin...");
        
        configManager = new ConfigManager(dataFolder, logger);
        if (!configManager.loadConfig()) {
            logger.error("Failed to load configuration!");
            return;
        }

        whitelistManager = new WhitelistManager(dataFolder, logger);
        if (!whitelistManager.load()) {
            logger.error("Failed to load whitelist!");
            return;
        }

        pingHandler = new PingHandler(this);
        proxy.getEventManager().register(this, pingHandler);
        
        maintenanceHandler = new MaintenanceHandler(this);
        proxy.getEventManager().register(this, maintenanceHandler);
        
        proxy.getCommandManager().register("tgmotd", new MotdCommand(this));
        proxy.getCommandManager().register("whitelist", new WhitelistCommand(this));
        
        logger.info("TG-MOTD has been enabled successfully!");
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        logger.info("Shutting down TG-MOTD...");
    }

    public void reloadPlugin() {
        configManager.loadConfig();
        whitelistManager.load();
        pingHandler.refresh();
        logger.info("Configuration reloaded!");
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Logger getLogger() {
        return logger;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }
}
