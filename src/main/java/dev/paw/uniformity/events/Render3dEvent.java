package dev.paw.uniformity.events;

import net.minecraft.client.util.math.MatrixStack;

public class Render3dEvent extends Event {
    public final MatrixStack matrices;
    public final float tickDelta;

    public Render3dEvent(MatrixStack matrixStack, float delta) {
        matrices = matrixStack;
        tickDelta = delta;
    }
}
