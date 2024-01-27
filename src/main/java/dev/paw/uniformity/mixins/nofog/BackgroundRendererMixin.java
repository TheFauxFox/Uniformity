package dev.paw.uniformity.mixins.nofog;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.paw.uniformity.Uniformity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BackgroundRenderer.class, priority = 910)
public class BackgroundRendererMixin {
    @Inject(method = "applyFog", at = @At("RETURN"))
    private static void applyFogModifyDistance(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if (Uniformity.config.noFogToggle) {
            RenderSystem.setShaderFogStart(-8.0F);
            RenderSystem.setShaderFogEnd(1000000.0F);
            RenderSystem.setShaderFogShape(FogShape.CYLINDER);
        }
    }
}
