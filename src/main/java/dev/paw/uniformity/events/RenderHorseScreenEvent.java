package dev.paw.uniformity.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.passive.AbstractHorseEntity;

public class RenderHorseScreenEvent extends Event {
    public final DrawContext ctx;
    public final AbstractHorseEntity horse;
    public RenderHorseScreenEvent(DrawContext context, AbstractHorseEntity entity) {
        ctx = context;
        horse = entity;
    }
}
