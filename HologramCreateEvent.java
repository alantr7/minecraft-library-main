package com.alant7_.util.event;

import com.alant7_.util.holograms.Hologram;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HologramCreateEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private Hologram hologram;

    public HologramCreateEvent (Hologram hologram) {
        this.hologram = hologram;
    }

    public Hologram getHologram() {
        return hologram;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
