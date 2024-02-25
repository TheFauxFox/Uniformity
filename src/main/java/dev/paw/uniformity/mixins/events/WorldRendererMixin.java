package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.Render3dEvent;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow @Final private BufferBuilderStorage bufferBuilders;

    @Inject(method = "render", at = @At("TAIL"))
    private void onWorldRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        Uniformity.bus.post(new Render3dEvent(matrices, tickDelta, camera, bufferBuilders));
    }
}

