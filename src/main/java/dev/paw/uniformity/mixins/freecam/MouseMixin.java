package dev.paw.uniformity.mixins.freecam;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.Freecam;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow private double eventDeltaVerticalWheel;

    @Inject(method = "onMouseScroll", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;eventDeltaVerticalWheel:D", ordinal = 7), cancellable = true)
    private void onMouseScroll(CallbackInfo ci) {
        if (eventDeltaVerticalWheel != 0.0) {
            Freecam fc = Uniformity.getModule(Freecam.class);
            if (fc == null) return;
            if (Uniformity.config.freecamToggle && !Uniformity.zoom.isZooming) {
                fc.speed += eventDeltaVerticalWheel > 0.0 ? 0.05D : -0.05D;
                fc.speed = Math.max(-0.5, fc.speed);
                fc.speed = Math.min(fc.speed, 20);
                ci.cancel();
            }
        }
    }
}
