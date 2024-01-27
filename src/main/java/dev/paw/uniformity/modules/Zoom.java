package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;

public class Zoom extends Module {

    public int zoomStep = 0;
    private final double defaultZoomDivisor = 4;
    private double zoomDivisor = defaultZoomDivisor;
    public boolean isZooming = false;
    public boolean wasZooming = false;
    public boolean transitionActive = false;
    private double fovMultiplier;
    private float internalMultiplier = 1;
    private float lastInternalMultiplier = 1;

    public Zoom() {
        super("Zoom");
    }

    public double applyZoom(double fov, float tickDelta) {
        fovMultiplier = MathHelper.lerp(tickDelta, this.lastInternalMultiplier, this.internalMultiplier);
        return fov * fovMultiplier;
    }

    public void tick() {
        double zoomMultiplier = 1.0F / (isZooming ? zoomDivisor : 1);

        this.lastInternalMultiplier = this.internalMultiplier;

        float smoothMultiplier = 0.5f;
        this.internalMultiplier += (float) ((zoomMultiplier - internalMultiplier) * smoothMultiplier);

        if (isZooming || fovMultiplier == this.internalMultiplier) {
            transitionActive = isZooming;
        }
    }

    public double applyXModifier(double cursorDeltaX) {
        return cursorDeltaX * (transitionActive ? internalMultiplier : 1.0);
    }

    public double applyYModifier(double cursorDeltaY) {
        return cursorDeltaY * (transitionActive ? internalMultiplier : 1.0);
    }

    public void changeZoomDivisor(boolean increase) {
        int upperScrollStep = 10;
        int lowerScrollStep = 5;
        zoomStep = increase ? Math.min(zoomStep + 1, upperScrollStep) :  Math.max(zoomStep - 1, -lowerScrollStep);

        if (zoomStep > 0) {
            double maximumZoomDivisor = 50.0;
            zoomDivisor = defaultZoomDivisor + ((maximumZoomDivisor - defaultZoomDivisor) / upperScrollStep * zoomStep);
        } else if (zoomStep == 0) {
            zoomDivisor = defaultZoomDivisor;
        } else {
            double minimumZoomDivisor = 1.0;
            zoomDivisor = defaultZoomDivisor + ((minimumZoomDivisor - defaultZoomDivisor) / lowerScrollStep * -zoomStep);
        }
    }

    public void unbindConflictingKey(MinecraftClient client) {
        if (Uniformity.zoomKeybind.isDefault()) {
            if (client.options.saveToolbarActivatorKey.isDefault()) {
                client.options.saveToolbarActivatorKey.setBoundKey(InputUtil.UNKNOWN_KEY);
                client.options.write();
                KeyBinding.updateKeysByCode();
            }
        }
    }

    public void resetZoomDivisor() {
        zoomDivisor = defaultZoomDivisor;
    }



    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean value) {

    }
}
