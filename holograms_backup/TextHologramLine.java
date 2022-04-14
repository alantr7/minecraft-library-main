package com.alant7_.util.holograms;

import com.alant7_.util.Formatter;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TextHologramLine extends HologramLine<String> {
/*
    private EntityArmorStand stand;

    public TextHologramLine(String text, Hologram hologram, Location location) {
        super(text, hologram);

       /* this.stand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        this.stand.setCustomNameVisible(true);
        this.stand.setNoGravity(true);
        this.stand.setInvisible(true);

        if (hologram.isVisible())
            Bukkit.getOnlinePlayers().forEach(this::_showTo);
    }

    public String getText() {
        return getContent();
    }

    public void setText(String text) {
        setContent(text);
    }

    EntityArmorStand _getStand() {
        return stand;
    }

    @Override
    void _setContent(String s) {
        super._setContent(s);
    }

    void _setLocation(Location location) {
        stand.setLocation(location.getX(), location.getY(), location.getZ(), 0f, 0f);

        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(stand);

        Bukkit.getOnlinePlayers().forEach(player -> {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        });
    }

    void _remove() {
        Bukkit.getOnlinePlayers().forEach(this::_hideFrom);
        this.setContent(null);
        this.stand = null;
    }

    void _showTo(Player player) {

        PacketPlayOutSpawnEntityLiving packet1 = new PacketPlayOutSpawnEntityLiving(stand);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet1);

    }

    void _hideFrom(Player player) {

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(stand.getId());

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

    }

    void _update(Player player, String text) {

        stand.setCustomName(new ChatComponentText(Formatter.formatColors(text)));

        PacketPlayOutEntityMetadata packet2 = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);

    }*/

}
