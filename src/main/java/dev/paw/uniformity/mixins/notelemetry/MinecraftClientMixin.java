package dev.paw.uniformity.mixins.notelemetry;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "isTelemetryEnabledByApi", at = @At("HEAD"), cancellable = true)
    private void onIsTelemetryEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (Uniformity.config.disableTelemetryToggle) cir.setReturnValue(false);
    }
}
