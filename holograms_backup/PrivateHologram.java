package com.alant7_.util.holograms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrivateHologram extends Hologram {

    private final UUID playerId;

    PrivateHologram(int id, UUID playerId, Location location) {
        super(id, location);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    @Override
    void _update(Player player) {
        if (player.getUniqueId().equals(playerId))
            super._update(player);
    }

}
