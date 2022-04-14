package com.alant7_.util.event;

import com.alant7_.util.holograms.Hologram;
import com.alant7_.util.holograms.HologramLineContent;
import com.alant7_.util.holograms.Visibility;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class HologramUpdateEvent extends Event implements Cancellable {

    private static HandlerList handlerList = new HandlerList();

    private Player player;

    private Hologram hologram;

    private HologramLineContent[] lines;

    private boolean isCancelled = false;

    private Visibility visibility = Visibility.UNSET;

    public HologramUpdateEvent(Player player, Hologram hologram) {
        this.player = player;
        this.hologram = hologram;

        this.lines = new HologramLineContent[hologram.getLines().size()];

        for (int i = 0; i < hologram.getLines().size(); i++) {
            Object content = hologram.getLines().get(i).getContent();
            if (content instanceof ItemStack)
                lines[i] = new HologramLineContent((ItemStack) content);
            else {
                lines[i] = new HologramLineContent((String) content);
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public Object[] getLines() {
        return lines;
    }

    public void setLine(int index, HologramLineContent content) {
        lines[index] = content;
    }

    public HologramLineContent getLine(int index) {
        return lines[index];
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility v) {
        this.visibility = v;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
