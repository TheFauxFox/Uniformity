package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.TelemetryEvent;
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
        if (Uniformity.bus.post(new TelemetryEvent()).isCancelled()) cir.setReturnValue(TelemetrySender.NOOP);
    }
}
