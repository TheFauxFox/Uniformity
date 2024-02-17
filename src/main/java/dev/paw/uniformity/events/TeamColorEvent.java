package dev.paw.uniformity.events;

import dev.paw.uniformity.utils.Color;
import net.minecraft.entity.Entity;

public class TeamColorEvent extends Event {
    private int color;
    public final Entity entity;

    public TeamColorEvent(Entity entity, int current) {
        color = current;
        this.entity = entity;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color.asInt;
    }

    public int getColor() {
        return color;
    }
}
