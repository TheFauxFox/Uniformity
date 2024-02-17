package dev.paw.uniformity.events;

import net.minecraft.entity.Entity;

public class EntityHasLabelEvent extends Event {
    public final Entity entity;
    private boolean label;
    public EntityHasLabelEvent(Entity entity, boolean label) {
        this.entity = entity;
        this.label = label;
    }

    public boolean hasLabel() {
        return label;
    }

    public void setHasLabel(boolean label) {
        this.label = label;
    }
}
