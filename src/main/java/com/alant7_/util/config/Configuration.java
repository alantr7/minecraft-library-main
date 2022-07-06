package com.alant7_.util.config;

import com.alant7_.util.BukkitPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public abstract class Configuration {

    private FileConfiguration config;
    private final String path;

    private final BukkitPlugin plugin;

    public Configuration(BukkitPlugin plugin, String path) {
        this.plugin = plugin;
        this.path = path;
        plugin.getConfigurationManager().register(this);
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveDefaultConfig(path);
        }

        onConfigReady(config = YamlConfiguration.loadConfiguration(file));
    }

    public void save() {
        try {
            config.save(getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return new File(plugin.getDataFolder(), path);
    }

    public int getInteger(String key, int defaultValue) {
        return getNumberOrDefault(key, defaultValue).intValue();
    }

    public long getLong(String key, long defaultValue) {
        return getNumberOrDefault(key, defaultValue).longValue();
    }

    public double getDouble(String key, double defaultValue) {
        return getNumberOrDefault(key, defaultValue).doubleValue();
    }

    public Number getNumber(String key) {
        return getNumberOrDefault(key, 0);
    }

    public Number getNumberOrDefault(String key, Number defaultValue) {
        Object object = config.get(key);
        if (object instanceof Number) {
            return (Number) object;
        }
        return defaultValue;
    }

    public ConfigurationItem getItem(String key) {
        return new ConfigurationItem(Objects.requireNonNull(config.getConfigurationSection(key)));
    }

    public abstract void onConfigReady(FileConfiguration config);

    public FileConfiguration getConfig() {
        return config;
    }

    public static void register(Configuration util) {
        util.load();
    }

}
