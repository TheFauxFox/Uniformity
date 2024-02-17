package dev.paw.uniformity.events;

import net.minecraft.client.util.math.MatrixStack;

public class RenderFireOverlayEvent extends CancellableEvent {
    public final MatrixStack matrixStack;
    public RenderFireOverlayEvent(MatrixStack matricies) {
        matrixStack = matricies;
    }
}
