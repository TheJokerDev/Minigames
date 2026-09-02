package com.j0keer.minigames.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final File configFolder;
    private final Map<String, ConfigFile> configs;

    public ConfigManager() {
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve("minigames");
        this.configFolder = configDir.toFile();
        this.configs = new HashMap<>();

        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
    }

    public void loadConfigs() {
        createConfig("maps.json");
    }

    public ConfigFile createConfig(String name) {
        String fileName = name.endsWith(".json") ? name : name + ".json";
        if (configs.containsKey(fileName)) {
            return configs.get(fileName);
        }

        File file = new File(configFolder, fileName);
        ConfigFile configFile = new ConfigFile(file);
        configs.put(fileName, configFile);
        return configFile;
    }

    public ConfigFile getConfig(String name) {
        String fileName = name.endsWith(".json") ? name : name + ".json";
        if (!configs.containsKey(fileName))
            return createConfig(fileName);

        return configs.get(fileName);
    }

    public ConfigFile getConfigFile(String name) {
        return getConfig(name);
    }

    public void saveConfig(String name) {
        String fileName = name.endsWith(".json") ? name : name + ".json";
        ConfigFile configFile = configs.get(fileName);
        if (configFile != null)
            configFile.save();
    }

    public void saveAll() {
        for (ConfigFile config : configs.values()) {
            config.save();
        }
    }

    public void reloadConfig(String name) {
        String fileName = name.endsWith(".json") ? name : name + ".json";
        ConfigFile configFile = configs.get(fileName);
        if (configFile != null)
            configFile.reload();
    }

    public void reloadAll() {
        for (ConfigFile config : configs.values()) {
            config.reload();
        }
    }

    public ConfigFile getMapsConfig() {
        return getConfig("maps.json");
    }

    public Map<String, ConfigFile> getConfigs() {
        return Collections.unmodifiableMap(configs);
    }

    public File getConfigFolder() {
        return configFolder;
    }
}
