package com.alant7_.util.holograms;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.event.HologramCreateEvent;
import com.alant7_.util.event.HologramDeleteEvent;
import com.alant7_.util.event.HologramUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.*;

public class HologramManager implements Listener {

    private static Map<Class<? extends AlanJavaPlugin>, HologramManager> hologramManagerMap = new HashMap<>();

    private AlanJavaPlugin plugin;

    private Map<Integer, Hologram> holograms = new HashMap<>();

    private HologramManager (AlanJavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public Hologram createHologram(Location location) {

        Optional<Integer> maxOptional  = holograms.keySet().stream().max(Integer::compareTo);

        int max = maxOptional.orElse(0);

        Hologram hologram = new Hologram(max + 1, location);

        hologram.setPlugin(plugin);

        holograms.put(max + 1, hologram);

        HologramCreateEvent event = new HologramCreateEvent(hologram);

        Bukkit.getPluginManager().callEvent(event);

        return hologram;

    }

    public PrivateHologram createPrivateHologram(Player player, Location location) {

        Optional<Integer> maxOptional  = holograms.keySet().stream().max(Integer::compareTo);

        int max = maxOptional.orElse(0);

        PrivateHologram hologram = new PrivateHologram(max + 1, player.getUniqueId(), location);

        hologram.setPlugin(plugin);

        holograms.put(max + 1, hologram);

        HologramCreateEvent event = new HologramCreateEvent(hologram);

        Bukkit.getPluginManager().callEvent(event);

        return hologram;

    }

    public void removeHologram(Integer id) {

        Hologram hologram = holograms.get(id);

        if (hologram == null) {
            return;
        }

        holograms.remove(id);

        File dir = new File(plugin.getDataFolder(), "holograms");
        dir.mkdirs();

        File file = new File(dir, id + ".yml");
        if (file.exists())
            file.delete();

        HologramDeleteEvent event = new HologramDeleteEvent(hologram);

        Bukkit.getPluginManager().callEvent(event);

        hologram._destroy();

    }

    public void removeHologram(Hologram hologram) {
        removeHologram(hologram.getId());
    }

    public Hologram getHologram(Integer id) {
        return holograms.get(id);
    }

    public PrivateHologram getPrivateHologram(Integer id) {
        Hologram hologram = holograms.get(id);
        return hologram instanceof PrivateHologram ? (PrivateHologram) hologram : null;
    }

    void saveHologram(Hologram hologram) {
        File dir = new File(plugin.getDataFolder(), "holograms");
        dir.mkdirs();

        File holoFile = new File(dir, hologram.getId() + ".yml");
        plugin.createEmptyFile(holoFile);

        FileConfiguration config = YamlConfiguration.loadConfiguration(holoFile);
        config.set("hologram", hologram);

        try {
            config.save(holoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveHolograms() {
        holograms.forEach((id, holo) -> saveHologram(holo));
    }

    public void loadHolograms() {

        holograms.clear();

        File dir = new File(plugin.getDataFolder(), "holograms");
        dir.mkdirs();

        File[] files = dir.listFiles();

        if (files != null) {
            for (File f : files) {

                FileConfiguration config = YamlConfiguration.loadConfiguration(f);

                Hologram hologram = config.getObject("hologram", Hologram.class);

                if (hologram != null) {
                    hologram.setPlugin(plugin);

                    holograms.put(hologram.getId(), hologram);
                }

            }
        }

    }

    public void unloadHolograms() {

        List<Hologram> list = new ArrayList<>(holograms.values());

        list.forEach(Hologram::remove);

    }

    public static HologramManager getInstance(Class<? extends AlanJavaPlugin> plugin) {
        return hologramManagerMap.getOrDefault(plugin, null);
    }

    public static Collection<HologramManager> getInstances() {
        return hologramManagerMap.values();
    }

    public static void registerPlugin(AlanJavaPlugin plugin) {
        if (hologramManagerMap.containsKey(plugin.getClass()))
            return;

        hologramManagerMap.put(plugin.getClass(), new HologramManager(plugin));

        Bukkit.getConsoleSender().sendMessage("§b[Hologram Manager] §fRegistered plugin " + plugin.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        holograms.forEach((id, holo) -> {
            if (holo.isVisible()) {
                holo.getLines().forEach(hologramLine -> {
                    hologramLine._showTo(event.getPlayer());
                });

                holo._update(event.getPlayer());
            }
        });
    }

}
