package dev.paw.uniformity.events;

import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class Render3dEvent extends Event {
    public final MatrixStack matrices;
    public final float tickDelta;
    public final Camera camera;

    public Render3dEvent(MatrixStack matrixStack, float delta, Camera camera) {
        matrices = matrixStack;
        tickDelta = delta;
        this.camera = camera;
    }
}
