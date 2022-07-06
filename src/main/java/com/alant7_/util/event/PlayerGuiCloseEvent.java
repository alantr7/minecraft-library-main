package com.alant7_.util.event;

import com.alant7_.util.gui.AbstractGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGuiCloseEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private Player player;

    private AbstractGUI gui;

    public PlayerGuiCloseEvent(Player player, AbstractGUI gui) {
        this.player = player;
        this.gui = gui;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractGUI getGUI() {
        return gui;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
