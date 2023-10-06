package net.lumania.economy.utils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    /* Variables */
    private final Logger logger;
    private final File file;
    @Getter private FileConfiguration config;


    /* Constructor */
    public ConfigManager(Plugin plugin, String name) {
        this.logger = LoggerFactory.getLogger(ConfigManager.class);
        this.file = new File(plugin.getDataFolder(), name.replace(".yml", "") + ".yml");

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name.replace(".yml", "") + ".yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /* Methods */
    public void save() {
        try {
            config.save(file);
            reload();
        } catch (IOException exception) {
            logger.error("[!] FAILED TO SAVE A CONFIG FILE", exception);
        }
    }

    public void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception exception) {
            logger.error("[!] FAILED TO RELOAD A CONFIG FILE", exception);
        }
    }

    public String getString(String path) {
        if(config.contains(path)) {
            return config.getString(path);
        }
        return null;
    }

    public int getInt(String path) {
        if(config.contains(path)) {
            return config.getInt(path);
        }
        return 0;
    }

    public String getFormatted(String path) {
        return ChatColor.translateAlternateColorCodes('&', getString(path));
    }
}
