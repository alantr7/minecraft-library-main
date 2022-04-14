package com.alant7_.util.holograms;

import com.alant7_.util.AlanJavaPlugin;
import com.alant7_.util.event.HologramUpdateEvent;
import com.alant7_.util.objects.TemporaryMetadata;
import com.alant7_.util.reflections.Pair;
import com.alant7_.util.reflections.ReflectionsUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Hologram implements ConfigurationSerializable {

    private List<HologramLine<?>> lines = new ArrayList<>();

    private boolean isVisible = true;

    private Location location;

    private int id;

    private AlanJavaPlugin plugin;

    private final TemporaryMetadata metadata;

    Hologram (int id, Location location) {
        this.id = id;
        this.location = location;
        this.metadata = new TemporaryMetadata();
    }

    public AlanJavaPlugin getPlugin() {
        return plugin;
    }

    final void setPlugin(AlanJavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addTextLine(String line) {
        Location location = this.location.clone();
        location.setY(getNextY(LineType.TEXT));

        TextHologramLine holoLine = new TextHologramLine(line, this, location);
        lines.add(holoLine);

        if (isVisible) {
            _update();
        }
    }

    public void addItemLine(ItemStack stack) {
        Location location = this.location.clone();
        location.setY(getNextY(LineType.ITEM));

        ItemHologramLine hologramLine = new ItemHologramLine(stack, this, location);
        lines.add(hologramLine);

        if (isVisible) {
            _update();
        }
    }

    public void removeLine(int index) {
        if (index < lines.size()) {
            lines.remove(index)._remove();
        }
    }

    public void setLocation(Location location) {
        this.location = location;

        AtomicDouble y = new AtomicDouble(location.getY());

        lines.forEach(line -> {
            double lineY = line.getType() == LineType.TEXT ? (y.get() - 0.32) : (y.get() + 1.45);
            line._setLocation(new Location(location.getWorld(), location.getX(), lineY, location.getZ()));

            if (line.getType() == LineType.TEXT)
                y.set(y.get() - 0.32);
            else {
                y.set(y.get() - 0.8);
            }
        });
    }

    private double getNextY(LineType type) {

        double y = location.getY();

        for (HologramLine<?> line : lines) {
            if (line.getType() == LineType.TEXT) {
                y -= 0.32;
            } else {
                y -= 0.8;
            }
        }

        return type == LineType.TEXT ? (y - 0.32) : (y + 1.45);

    }

    public void clearLines() {
        lines.forEach(HologramLine::_remove);
        lines.clear();
    }

    public int getLinesCount() {
        return lines.size();
    }

    public List<HologramLine<?>> getLines() {
        return new ArrayList<>(lines);
    }

    public void save() {
        HologramManager.getInstance(plugin.getClass()).saveHologram(this);
    }

    public void remove() {
        HologramManager.getInstance(plugin.getClass()).removeHologram(id);
    }

    public int getId() {
        return id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean b) {
        if (b == isVisible)
            return;

        isVisible = b;

        if (b) {
            Bukkit.getOnlinePlayers().forEach(player -> lines.forEach(line -> {
                line._showTo(player);
            }));
            _update();
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> lines.forEach(line -> line._hideFrom(player)));
        }
    }

    public Location getLocation() {
        return location;
    }

    public TemporaryMetadata getMetadata() {
        return metadata;
    }

    public void update() {
        _update();
    }

    public void update(Player player) {
        _update(player);
    }

    void _update() {
        Bukkit.getOnlinePlayers().forEach(this::_update);
    }

    void _update(Player player) {

        HologramUpdateEvent event = new HologramUpdateEvent(player, this);

        Bukkit.getPluginManager().callEvent(event);

        if (event.getVisibility() == Visibility.UNSET) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i) instanceof TextHologramLine) {
                    ((TextHologramLine) lines.get(i))._update(player, event.getLine(i).getText());
                } else {
                    ((ItemHologramLine) lines.get(i))._update(player, event.getLine(i).getItemStack());
                }
            }
        } else if (event.getVisibility() == Visibility.INVISIBLE) {
            for (int i = 0; i < lines.size(); i++) {
                lines.get(i)._hideFrom(player);
            }
        } else {
            for (int i = 0; i < lines.size(); i++) {
                lines.get(i)._showTo(player);
                if (lines.get(i) instanceof TextHologramLine) {
                    ((TextHologramLine) lines.get(i))._update(player, event.getLine(i).getText());
                } else {
                    ((ItemHologramLine) lines.get(i))._update(player, event.getLine(i).getItemStack());
                }
            }
        }

    }

    void _destroy() {
        clearLines();
    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();

        map.put("id", id);

        map.put("location", location);

        map.put("lines", lines);

        return map;

    }

    @SuppressWarnings(value = "unchecked")
    public static Hologram deserialize(Map<String, Object> map) {

        Hologram hologram = new Hologram((int) map.get("id"), (Location) map.get("location"));

        List<String> lines = (List<String>) map.get("lines");

        lines.forEach(hologram::addTextLine);

        return hologram;

    }

    public static Hologram valueOf(Map<String, Object> map) {
        return deserialize(map);
    }

}
