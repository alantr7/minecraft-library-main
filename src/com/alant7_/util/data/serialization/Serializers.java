package com.alant7_.util.data.serialization;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Serializers {

    public static final FieldSerializer<String> STRING = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getString);

    public static final FieldSerializer<Integer> INTEGER = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getInt);

    public static final FieldSerializer<Long> LONG = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getLong);

    public static final FieldSerializer<Double> DOUBLE = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getDouble);

    public static final FieldSerializer<Location> LOCATION = new FieldSerializer<>(ConfigurationSection::set, ConfigurationSection::getLocation);

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
