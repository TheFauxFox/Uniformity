package dev.paw.uniformity.mixins.events;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.Render3dEvent;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
//    @Shadow @Final private BufferBuilderStorage bufferBuilders;
//
//    @Inject(method = "render", at = @At("TAIL"))
//    private void onWorldRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local MatrixStack matrixStack) {
//        matrixStack.multiplyPositionMatrix(matrix4f2);
//        Uniformity.bus.post(new Render3dEvent(matrixStack, tickCounter.getTickDelta(false), camera, bufferBuilders));
//    }
//
//    @Inject(
//        at = @At(value = "FIELD",
//            target = "renderHand",
//            opcode = Opcodes.GETFIELD,
//            ordinal = 0),
//        method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V")
//    private void onRenderWorldHandRendering(RenderTickCounter tickCounter,
//                                            CallbackInfo ci, @Local(ordinal = 1) Matrix4f matrix4f2,
//                                            @Local(ordinal = 1) float tickDelta)
//    {
//        MatrixStack matrixStack = new MatrixStack();
//        matrixStack.multiplyPositionMatrix(matrix4f2);
//        RenderEvent event = new RenderEvent(matrixStack, tickDelta);
//        EventManager.fire(event);
//    }
}

