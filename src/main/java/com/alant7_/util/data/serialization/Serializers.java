package com.alant7_.util.data.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.util.UUID;

public class Serializers {

    public static final FieldSerializer<String> STRING = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getString);

    public static final FieldSerializer<Integer> INTEGER = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getInt);

    public static final FieldSerializer<Long> LONG = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getLong);

    public static final FieldSerializer<Double> DOUBLE = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getDouble);

    public static final FieldSerializer<Location> LOCATION = new FieldSerializer<>((section, key, value) -> {
        if (value == null) {
            section.set(key, null);
            return;
        }
        section.set(key, String.format("%s,%f,%f,%f,%f,%f", value.getWorld().getName(), value.getX(), value.getY(), value.getZ(), value.getYaw(), value.getPitch()));
    }, (section, key) -> {
        Object object = section.get(key);
        if (object instanceof Location) {
            return (Location) object;
        } else if (object instanceof String) {
            String[] values = ((String) object).split(",");
            if (values.length < 4)
                return null;

            Location location = new Location(Bukkit.getWorld(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]), Double.parseDouble(values[3]));
            if (values.length > 4)
                location.setYaw(Float.parseFloat(values[4]));
            if (values.length > 5)
                location.setPitch(Float.parseFloat(values[5]));

            return location;
        }
        return null;
    });

    public static final FieldSerializer<Location> BLOCK_LOCATION = new FieldSerializer<>((section, key, value) -> {
        if (value == null) {
            section.set(key, null);
            return;
        }
        section.set(key, String.format("%s,%d,%d,%d", value.getWorld().getName(), value.getBlockX(), value.getBlockY(), value.getBlockZ()));
    }, (section, key) -> {
        Object object = section.get(key);
        if (object instanceof Location) {
            return (Location) object;
        } else if (object instanceof String) {
            String[] values = ((String) object).split(",");
            if (values.length < 4)
                return null;

            return new Location(Bukkit.getWorld(values[0]), Math.floor(Double.parseDouble(values[1])), Math.floor(Double.parseDouble(values[2])), Math.floor(Double.parseDouble(values[3])));
        }
        return null;
    });

    public static final FieldSerializer<ItemStack> ITEMSTACK = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getItemStack);

    public static final FieldSerializer<Boolean> BOOLEAN = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getBoolean);

    public static final FieldSerializer<UUID> UUID = new FieldSerializer<>(
            (section, key, val) -> section.set(key, val != null ? val.toString() : null),
            (section, key) -> {
                String val = section.getString(key);
                return val != null ? java.util.UUID.fromString(val) : null;
            }
    );

}
