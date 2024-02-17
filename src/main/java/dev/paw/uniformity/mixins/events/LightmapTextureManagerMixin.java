package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.GetBrightnessEvent;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void onGetBrightness(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(Uniformity.bus.post(new GetBrightnessEvent(cir.getReturnValue())).getBrightness());
    }
}