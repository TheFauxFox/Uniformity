package dev.paw.uniformity.mixins.notelemetry;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.session.telemetry.TelemetryManager;
import net.minecraft.client.session.telemetry.TelemetrySender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TelemetryManager.class)
public class TelemetryManagerMixin {
    @Inject(method = "computeSender", at = @At("HEAD"), cancellable = true)
    private void onGetTelemetrySender(CallbackInfoReturnable<TelemetrySender> cir) {
        if (Uniformity.config.disableTelemetryToggle) cir.setReturnValue(TelemetrySender.NOOP);
    }
}
