package dev.paw.uniformity.events;

import net.minecraft.client.gui.DrawContext;

public class Render2dEvent extends Event {
    public final DrawContext ctx;
    public final float tickDelta;

    public Render2dEvent(DrawContext context, float delta) {
        ctx = context;
        tickDelta = delta;
    }

    public boolean isHudHidden() {
        return client.options.hudHidden;
    }

    public boolean isDebugShowing() {
        return client.getDebugHud().shouldShowDebugHud();
    }

    public boolean hideHud() {
        return isHudHidden() || isDebugShowing();
    }
}
