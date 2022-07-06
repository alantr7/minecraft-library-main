package com.alant7_.util;

import com.alant7_.util.chat.MessageCollector;
import com.alant7_.util.command.CommandExecutor;
import com.alant7_.util.config.ConfigurationManager;
import com.alant7_.util.data.IDataLoader;
import com.alant7_.util.data.IDataSource;
import com.alant7_.util.data.managers.players.PlayerDataManager;
import com.alant7_.util.data.serialization.YamlSerializer;
import com.alant7_.util.event.listeners.EventListenerArmorEquip;
import com.alant7_.util.event.listeners.EventListenerBrewing;
import com.alant7_.util.event.listeners.EventListenerMessageCollector;
import com.alant7_.util.gui.GUIManager;
import com.alant7_.util.modules.HologramsModule;
import com.alant7_.util.nms.MinecraftVersion;
import com.alant7_.util.objects.Holder;
import com.alant7_.util.wrapper.events.PluginEventListener;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class BukkitPlugin extends JavaPlugin {

    private static final HashMap<Class<? extends BukkitPlugin>, BukkitPlugin> plugins = new HashMap<>();

    private MinecraftVersion minecraftVersion;

    private IDataSource dataSource;

    private IDataLoader dataLoader;

    private PlayerDataManager<?, ?> playerDataManager;

    private ConfigurationManager configurationManager;

    private GUIManager guiManager;

    private YamlSerializer yamlSerializer;

    private MessageCollector messageCollector;

    private PluginEventListener pluginEventListener;
    private boolean isEventListenerLocked = false;

    private final Holder<HologramsModule> moduleHolograms = new Holder<>();

    private final PluginSettings settings = new PluginSettings();

    @Override
    public final void onEnable() {

        plugins.put(getClass(), this);

        String packageName = Bukkit.getServer().getClass().getPackageName();
        this.minecraftVersion = TryCatch.perform(() -> MinecraftVersion.valueOf(packageName.substring(packageName.lastIndexOf('.') + 1)), MinecraftVersion.NOT_FOUND);

        configurationManager = new ConfigurationManager();

        pluginEventListener = new PluginEventListener(this);

        messageCollector = new MessageCollector();

        if (dataLoader != null)
            dataLoader.load();

        onPluginSetup(settings);

        if (settings.isEventListenerEnabled(PluginSettings.EventListener.ARMOR_EQUIP))
            try {
                Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
                Bukkit.getPluginManager().registerEvents(new EventListenerArmorEquip(new ArrayList<>()), this);
            } catch (Exception e) {
            }

        if (settings.isEventListenerEnabled(PluginSettings.EventListener.BREWING))
            registerEvents(new EventListenerBrewing(this));

        if (settings.isFeatureEnabled(PluginSettings.Feature.GUI))
            registerEvents((guiManager = new GUIManager()).getEventListener());

        if (settings.isFeatureEnabled(PluginSettings.Feature.YAML_SERIALIZER))
            yamlSerializer = new YamlSerializer();

        if (settings.isFeatureEnabled(PluginSettings.Feature.HOLOGRAM)) {
            moduleHolograms.set(new HologramsModule(this));
            moduleHolograms.get().enable();
        }

        registerEvents(new EventListenerMessageCollector(this));

        settings.lock();

        onPluginEnable();

    }

    @Override
    public final void onDisable() {

        if (guiManager != null)
            guiManager.close();

        Bukkit.getScheduler().cancelTasks(this);
        hook("ProtocolLib", () -> ProtocolLibrary.getProtocolManager().removePacketListeners(this));

        onPluginDisable();

        if (moduleHolograms.has())
            moduleHolograms.get().disable();

    }

    public void onPluginSetup(PluginSettings settings) {}

    public abstract void onPluginEnable();

    public abstract void onPluginDisable();

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static <T extends BukkitPlugin> T getInstance(Class<T> plugin) {
        return plugin.cast(plugins.getOrDefault(plugin, null));
    }

    public void createEmptyFile(String path) {
        createEmptyFile(new File(getDataFolder(), path));
    }

    public void createEmptyFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDefaultConfig(String name) {
        saveDefaultConfig(name, name);
    }

    public void saveDefaultConfig(String path, String name) {
        File file = new File(getDataFolder(), name);
        if (file.exists())
            return;

        getDataFolder().mkdir();

        try {
            InputStream is = this.getResource(path);
            if (is == null)
                return;

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            FileOutputStream fos = new FileOutputStream(file);

            String line;
            while ((line = br.readLine()) != null)
                fos.write((line + System.lineSeparator()).getBytes());

            fos.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void depend(String plugin, Runnable runnable) {
        if (Bukkit.getPluginManager().isPluginEnabled(plugin))
            runnable.run();
    }

    public void hook(String plugin, Runnable runnable) {
        if (Bukkit.getPluginManager().isPluginEnabled(plugin))
            runnable.run();
    }

    public void hook(String[] plugins, Runnable runnable) {
        for (String plugin : plugins) {
            if (!Bukkit.getPluginManager().isPluginEnabled(plugin))
                return;
        }

        runnable.run();
    }

    public void registerEvents(Listener... listeners) {
        for (Listener listener : listeners)
            Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerCommands(CommandExecutor... executors) {
        for (CommandExecutor executor : executors)
            executor.register(this);
    }

    public void setDataLoader(IDataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public void setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public IDataLoader getDataLoader() {
        return dataLoader;
    }

    public IDataSource getDataSource() {
        return dataSource;
    }

    protected void setPlayerDataManager(PlayerDataManager<?, ?> dataManager) {
        this.playerDataManager = dataManager;
    }

    public PlayerDataManager<?, ?> getPlayerDataManager() {
        return playerDataManager;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public @NotNull PluginEventListener getEventListener() {
        return pluginEventListener;
    }

    public void setEventListener(PluginEventListener pluginEventListener) {
        if (isEventListenerLocked) {
            return;
        }

        this.pluginEventListener = pluginEventListener;
        Bukkit.getPluginManager().registerEvents(pluginEventListener, this);

        isEventListenerLocked = true;
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public com.alant7_.util.holograms.api.HologramManager getHologramManager() {
        return moduleHolograms.get().getHologramManager();
    }

    public YamlSerializer getYamlSerializer() {
        return yamlSerializer;
    }

    public PluginSettings getSettings() {
        return settings;
    }

    public MinecraftVersion getMinecraftVersion() {
        return this.minecraftVersion;
    }

    public MessageCollector getMessageCollector() {
        return messageCollector;
    }

}
