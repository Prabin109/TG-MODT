package com.tgmotd.config;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WhitelistManager {

    private final Path dataFolder;
    private final Logger logger;
    private final Path playersFile;
    private List<String> whitelistedPlayers;

    public WhitelistManager(Path dataFolder, Logger logger) {
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.playersFile = dataFolder.resolve("players.yml");
        this.whitelistedPlayers = new ArrayList<>();
    }

    public boolean load() {
        try {
            if (!Files.exists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            if (!Files.exists(playersFile)) {
                createDefault();
            }

            Yaml yaml = new Yaml();
            try (InputStream input = Files.newInputStream(playersFile)) {
                Map<String, Object> data = yaml.load(input);
                if (data != null && data.containsKey("whitelisted-players")) {
                    Object players = data.get("whitelisted-players");
                    if (players instanceof List) {
                        whitelistedPlayers = new ArrayList<>((List<String>) players);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            logger.error("Error loading players.yml", e);
            return false;
        }
    }

    private void createDefault() throws IOException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("whitelisted-players", Arrays.asList("Prabin109"));
        
        Yaml yaml = new Yaml();
        try (Writer writer = Files.newBufferedWriter(playersFile)) {
            yaml.dump(data, writer);
        }
    }

    public boolean save() {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("whitelisted-players", whitelistedPlayers);
            
            Yaml yaml = new Yaml();
            try (Writer writer = Files.newBufferedWriter(playersFile)) {
                yaml.dump(data, writer);
            }
            return true;
        } catch (IOException e) {
            logger.error("Error saving players.yml", e);
            return false;
        }
    }

    public boolean isWhitelisted(String username) {
        return whitelistedPlayers.contains(username);
    }

    public boolean addPlayer(String username) {
        if (whitelistedPlayers.contains(username)) {
            return false;
        }
        whitelistedPlayers.add(username);
        return save();
    }

    public boolean removePlayer(String username) {
        if (!whitelistedPlayers.contains(username)) {
            return false;
        }
        whitelistedPlayers.remove(username);
        return save();
    }

    public List<String> getWhitelistedPlayers() {
        return new ArrayList<>(whitelistedPlayers);
    }
}
