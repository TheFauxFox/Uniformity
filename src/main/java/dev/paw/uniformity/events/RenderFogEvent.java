package dev.paw.uniformity.events;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;

public class RenderFogEvent extends Event {
    public final Camera camera;
    public final BackgroundRenderer.FogType fogType;
    public final float viewDistance;
    public final boolean thickFog;
    public final float tickDelta;
    public RenderFogEvent(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta) {
        this.camera = camera;
        this.fogType = fogType;
        this.viewDistance = viewDistance;
        this.thickFog = thickFog;
        this.tickDelta = tickDelta;
    }
}
