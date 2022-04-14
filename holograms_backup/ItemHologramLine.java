package com.alant7_.util.holograms;

import com.alant7_.util.nms.items.NMSItemStack;
import com.alant7_.util.reflections.ReflectionsUtil;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemHologramLine extends HologramLine<ItemStack> {

    private EntityItem item;

    private boolean customNameVisible = false;

    public ItemHologramLine(ItemStack stack, Hologram hologram, Location location) {
        super(stack, hologram);
        /*this.item = new EntityItem((World) ((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        ReflectionsUtil.invokeMethod(item, "setItemStack", new NMSItemStack(stack).nmsCopy());
        this.item.setNoGravity(true);

        if (hologram.isVisible())
            Bukkit.getOnlinePlayers().forEach(this::_showTo);*/

    }

    EntityItem _getItem() {
        return item;
    }

    void _setLocation(Location location) {
        /**item.setLocation(location.getX(), location.getY(), location.getZ(), 0f, 0f);

        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(item);

        Bukkit.getOnlinePlayers().forEach(player -> {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        });*/
    }

    void _remove() {
        Bukkit.getOnlinePlayers().forEach(this::_hideFrom);
        this.setContent(null);
        this.item = null;
    }

    void _showTo(Player player) {
/*
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        PacketPlayOutSpawnEntity packet2 = new PacketPlayOutSpawnEntity(item);

        PacketPlayOutEntityMetadata packet3 = new PacketPlayOutEntityMetadata(item.getId(), item.getDataWatcher(), true);

        PacketPlayOutEntityVelocity packet4 = new PacketPlayOutEntityVelocity(item.getId(), new Vec3D(0, 0, 0));

        entityPlayer.playerConnection.sendPacket(packet2);

        entityPlayer.playerConnection.sendPacket(packet3);

        entityPlayer.playerConnection.sendPacket(packet4);
*/
    }

    void _hideFrom(Player player) {
/*
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(item.getId());

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
*/
    }

    void _update(Player player, ItemStack stack) {
/*
        PacketPlayOutEntityMetadata packet2 = new PacketPlayOutEntityMetadata(item.getId(), item.getDataWatcher(), true);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);
*/
    }

    public void setItemStack(ItemStack stack) {
        setContent(stack);
        ReflectionsUtil.invokeMethod(item, "setItemStack", new NMSItemStack(stack).nmsCopy());
    }

    public ItemStack getItemStack() {
        return getContent();
    }

    public void setCustomNameVisible(boolean b) {
        /*customNameVisible = b;
        item.setCustomNameVisible(b);
        getHologram().update();*/
    }

    public void setCustomName(String name) {
        /*item.setCustomName(new ChatComponentText(name));
        getHologram().update();*/
    }

}
