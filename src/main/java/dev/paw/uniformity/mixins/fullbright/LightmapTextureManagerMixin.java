package dev.paw.uniformity.mixins.fullbright;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.Fullbright;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void onGetBrightness(CallbackInfoReturnable<Float> cir) {
        Fullbright fb = Uniformity.getModule(Fullbright.class);
        if (fb != null && fb.isEnabled()) {
            cir.setReturnValue(1f);
        }
    }
}
