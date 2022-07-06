package com.alant7_.util.config.updater;

import com.alant7_.util.BukkitPlugin;
import com.alant7_.util.TriConsumer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigUpdater {

    private final BukkitPlugin plugin;

    private final File file;

    private FileConfiguration config;

    private FileConfiguration internal;

    private final LinkedList<VersionPush> pushRequests = new LinkedList<>();

    public static final UpdateAction<UpdateAction.UpdateActionAdd> ADD = new UpdateAction<>((internal, config, key) -> {
        if (config.isSet(key))
            return;

        config.set(key, internal.get(key));
    });

    public static final UpdateAction<UpdateAction.UpdateActionMove> MOVE = new UpdateAction<>((config, keyOld, keyNew) -> {
        config.set(keyNew, config.get(keyOld));
        config.set(keyOld, null);
    });

    public static final UpdateAction<UpdateAction.UpdateActionRemove> REMOVE = new UpdateAction<>((config, key) -> config.set(key, null));

    public ConfigUpdater(BukkitPlugin plugin, String internal, File output) {
        this.plugin = plugin;
        this.readConfig(this.file = output);
        this.readInternal(internal);
    }

    private void readConfig(File output) {
        this.plugin.createEmptyFile(output);
        this.config = YamlConfiguration.loadConfiguration(output);
    }

    private void readInternal(String path) {
        try {
            InputStream is = plugin.getResource(path);
            if (is == null)
                return;

            InputStreamReader reader = new InputStreamReader(is);
            internal = YamlConfiguration.loadConfiguration(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VersionPush createUpdate(int version) {
        return new VersionPush(this, version);
    }

    public void commit() {
        for (var request : pushRequests) {
            request.commit();
        }
    }

    FileConfiguration getInternal() {
        return internal;
    }

    FileConfiguration getCurrent() {
        return config;
    }

    public int getVersion() {
        return config.getInt("FileVersion", 1);
    }

}
