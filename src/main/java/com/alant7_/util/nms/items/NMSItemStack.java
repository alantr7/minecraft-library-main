package com.alant7_.util.nms.items;

import com.alant7_.util.nms.NMSUtil;
import com.alant7_.util.reflections.Pair;
import com.alant7_.util.reflections.ReflectionsUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NMSItemStack {

    private Object nmsStack;

    public NMSItemStack(ItemStack stack) {
        nmsStack = ReflectionsUtil.invokeMethod(getCraftItemStackClass(), "asNMSCopy", new Pair<>(ItemStack.class, stack));
    }

    public Material getType() {
        return ReflectionsUtil.invokeMethod(nmsStack, "getType");
    }

    public NMSTagCompound getTag() {
        return new NMSTagCompound(ReflectionsUtil.invokeMethod(nmsStack, "getTag"));
    }

    public void setTag(NMSTagCompound tag) {
        ReflectionsUtil.invokeMethod(nmsStack, "setTag", tag.nmsTag);
    }

    public ItemStack bukkitCopy() {
        return ReflectionsUtil.invokeMethod(getCraftItemStackClass(), "asBukkitCopy", nmsStack);
    }

    public Object nmsCopy() {
        return nmsStack;
    }

    public void save(NMSTagCompound tag) {
        originalMethod("save", new Pair<>(NMSTagCompound.getNBTTagCompoundClass(), tag.nmsTag));
    }

    @SafeVarargs
    public final <T> T originalMethod(String method, Pair<Class<?>, Object>... params) {
        return ReflectionsUtil.invokeMethod(nmsStack, method, params);
    }

    public static Class<?> getCraftItemStackClass() {
        return ReflectionsUtil.findClass("org.bukkit.craftbukkit." + NMSUtil.getVersion() + ".inventory.CraftItemStack");
    }

}
