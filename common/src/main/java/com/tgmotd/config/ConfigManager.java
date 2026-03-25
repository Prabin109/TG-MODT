package com.tgmotd.config;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigManager {

    private final Path dataFolder;
    private final Logger logger;
    private final Path configFile;
    private Map<String, Object> config;

    public ConfigManager(Path dataFolder, Logger logger) {
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.configFile = dataFolder.resolve("config.yml");
    }

    public boolean loadConfig() {
        try {
            if (!Files.exists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            if (!Files.exists(configFile)) {
                createDefaultConfig();
            }

            Yaml yaml = new Yaml();
            try (InputStream input = Files.newInputStream(configFile)) {
                config = yaml.load(input);
            }
            return true;
        } catch (IOException e) {
            logger.error("Error loading config", e);
            return false;
        }
    }

    private void createDefaultConfig() throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/config.yml")) {
            if (in != null) {
                Files.copy(in, configFile);
            }
        }
    }

    public String getFormatter() {
        return getString("formatter", "MINIMESSAGE");
    }

    public List<String> getMotdLines() {
        return getStringList("motd.lines");
    }

    public List<String> getHoverInfo() {
        return getStringList("motd.hover");
    }

    public String getIcon() {
        return getString("motd.icon", "server-icon.png");
    }

    public int getMaxPlayers() {
        return getInt("players.max", 200);
    }

    public boolean showRealCount() {
        return getBoolean("players.show-real-count", true);
    }

    public int getFakePlayersAdd() {
        return getInt("players.fake.add", 0);
    }

    public int getFakePlayersPercent() {
        return getInt("players.fake.percent", 0);
    }

    public int getUpdateInterval() {
        return getInt("update.interval", 3000);
    }

    public boolean isMaintenanceEnabled() {
        return getBoolean("maintenance.enabled", false);
    }

    public boolean shouldKickOnJoin() {
        return getBoolean("maintenance.kick-on-join", true);
    }

    public String getKickMessage() {
        return getString("maintenance.kick-message", "<red>Server is under maintenance.</red>");
    }

    public List<String> getMaintenanceMotd() {
        return getStringList("maintenance.motd");
    }

    public List<String> getMaintenanceHover() {
        return getStringList("maintenance.hover");
    }

    public boolean overridePlayerCount() {
        return getBoolean("maintenance.player-count.override", false);
    }

    public int getMaintenanceOnline() {
        return getInt("maintenance.player-count.online", 0);
    }

    public int getMaintenanceMax() {
        return getInt("maintenance.player-count.max", 0);
    }

    private String getString(String path, String def) {
        Object value = getValueFromPath(path);
        return value != null ? value.toString() : def;
    }

    private int getInt(String path, int def) {
        Object value = getValueFromPath(path);
        return value instanceof Number ? ((Number) value).intValue() : def;
    }

    private boolean getBoolean(String path, boolean def) {
        Object value = getValueFromPath(path);
        return value instanceof Boolean ? (Boolean) value : def;
    }

    @SuppressWarnings("unchecked")
    private List<String> getStringList(String path) {
        Object value = getValueFromPath(path);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private Object getValueFromPath(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = config;
        
        for (int i = 0; i < keys.length - 1; i++) {
            Object next = current.get(keys[i]);
            if (next instanceof Map) {
                current = (Map<String, Object>) next;
            } else {
                return null;
            }
        }
        
        return current.get(keys[keys.length - 1]);
    }
}
