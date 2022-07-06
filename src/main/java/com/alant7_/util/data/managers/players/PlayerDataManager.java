package com.alant7_.util.data.managers.players;

import com.alant7_.util.BukkitPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public abstract class PlayerDataManager<T1 extends OfflinePlayerData, T2 extends PlayerData> {

    private final Map<UUID, T2> onlinePlayers = new HashMap<>();

    private final BukkitPlugin plugin;

    public PlayerDataManager(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    abstract void save(PlayerData data);

    abstract void save(OfflinePlayerData data);

    abstract void load(PlayerData data);

    abstract void load(OfflinePlayerData data);

    abstract void setDefaults(OfflinePlayerData data);

    abstract void setDefaults(PlayerData data);

    protected abstract T2 createInstance(UUID uuid, T1 offlinePlayer);

    protected abstract T1 createOfflineInstance(UUID uuid, File file, FileConfiguration data);

    public T2 getPlayer(UUID uuid) {
        return onlinePlayers.get(uuid);
    }

    public Collection<T2> getOnlinePlayers() {
        return Collections.unmodifiableCollection(onlinePlayers.values());
    }

    @SuppressWarnings("unchecked")
    public T1 getOfflinePlayer(UUID uuid) {
        if (onlinePlayers.containsKey(uuid))
            return (T1) onlinePlayers.get(uuid);

        File playersFolder = new File(plugin.getDataFolder(), "players");
        if (!playersFolder.exists())
            playersFolder.mkdir();

        File playerFile = new File(playersFolder, uuid + ".yml");
        if (!playerFile.exists())
            plugin.createEmptyFile(playerFile);

        FileConfiguration data = YamlConfiguration.loadConfiguration(playerFile);

        T1 t1 = createOfflineInstance(uuid, playerFile, data);
        load(t1);

        return t1;
    }

    public void load(Player player) {
        T1 offline = getOfflinePlayer(player.getUniqueId());
        if (offline == null) {

        }
    }

    public void unload(Player player) {

    }

}

