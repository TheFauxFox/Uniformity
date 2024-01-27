package dev.paw.uniformity.mixins.zoom;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import dev.paw.uniformity.Uniformity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Mouse;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Shadow private double eventDeltaVerticalWheel;

	@Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getInvertYMouse()Lnet/minecraft/client/option/SimpleOption;"))
	public void applyZoomChanges(CallbackInfo ci, @Local(ordinal = 1) double e, @Local(ordinal = 2) LocalDoubleRef k, @Local(ordinal = 3) LocalDoubleRef l, @Local(ordinal = 6) double h) {
		if (Uniformity.zoom.isZooming) {
			k.set(Uniformity.zoom.applyXModifier(k.get()));
			l.set(Uniformity.zoom.applyYModifier(l.get()));
		}
	}

	@Inject(method = "onMouseScroll", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;eventDeltaVerticalWheel:D", ordinal = 7), cancellable = true)
	private void onMouseScroll(CallbackInfo ci) {
		if (eventDeltaVerticalWheel != 0) {
			if (Uniformity.zoomKeybind.isPressed()) {
				Uniformity.zoom.changeZoomDivisor(eventDeltaVerticalWheel > 0);
				ci.cancel();
			}
		}
	}
}
