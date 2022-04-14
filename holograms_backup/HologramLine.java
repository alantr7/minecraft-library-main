package com.alant7_.util.holograms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class HologramLine<Content> {

    private final Hologram hologram;

    private Content content;

    final Class<?> contentClass;

    private final LineType type;

    protected HologramLine(Content content, Hologram hologram) {
        this.content = content;
        this.hologram = hologram;
        this.contentClass = content.getClass();
        this.type = this instanceof TextHologramLine ? LineType.TEXT : LineType.ITEM;
    }

    public Hologram getHologram() {
        return hologram;
    }

    abstract void _setLocation(Location location);

    abstract void _remove();

    abstract void _showTo(Player player);

    abstract void _hideFrom(Player player);

    abstract void _update(Player player, Content content);

    public void setContent(Content content) {
        this.content = content;
        this.hologram._update();
    }

    void _setContent(Content content) {
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public LineType getType() {
        return type;
    }

}
