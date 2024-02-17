package dev.paw.uniformity.events;

import net.minecraft.entity.Entity;

public class EntityOutlineEvent extends Event {
    private boolean outline;
    public final Entity entity;

    public EntityOutlineEvent(Entity entity, boolean current) {
        outline = current;
        this.entity = entity;
    }

    public boolean hasOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
    }
}
