package com.alant7_.util.skulls;

import com.alant7_.util.nms.NMSUtil;
import com.alant7_.util.reflections.ReflectionsUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class SkullFactory {

    public static final Class<?> skullClass;

    static {
        skullClass = ReflectionsUtil.findClass("org.bukkit.craftbukkit." + NMSUtil.getVersion() + ".block.CraftSkull");
    }

    public static ItemStack createSkull(String url) {

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        PropertyMap props = profile.getProperties();

        if (props == null) {
            return null;
        }

        byte[] encoded = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

        props.put("textures", new Property("textures", new String(encoded)));

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) head.getItemMeta();

        Class<?> clazz = meta.getClass();

        try {
            Field field = clazz.getDeclaredField("profile");

            field.setAccessible(true);

            field.set(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        head.setItemMeta(meta);

        return head;

    }

    public static void setSkull(Block block, String url) {

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        PropertyMap props = profile.getProperties();

        byte[] encoded = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

        props.put("textures", new Property("textures", new String(encoded)));

        Skull data = (Skull) block.getState();

        try {

            Field field = data.getClass().getDeclaredField("profile");

            field.setAccessible(true);

            field.set(data, profile);

            field.setAccessible(false);

            data.update();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
