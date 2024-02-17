package dev.paw.uniformity.events;

import net.minecraft.client.render.Camera;

public class GetFovEvent extends Event {
    public final Camera camera;
    public final float tickDelta;
    public final boolean changingFov;

    private double fov;

    public GetFovEvent(Camera camera, float tickDelta, boolean changingFov, double fov) {
        this.camera = camera;
        this.tickDelta = tickDelta;
        this.changingFov = changingFov;
        this.fov = fov;
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}
