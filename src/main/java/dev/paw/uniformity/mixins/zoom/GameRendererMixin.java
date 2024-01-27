package dev.paw.uniformity.mixins.zoom;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "tick()V", at = @At("HEAD"))
	private void tickInstances(CallbackInfo info) {
		if (Uniformity.zoom.isZooming || Uniformity.zoom.transitionActive) {
			Uniformity.zoom.tick();
		}
	}

	@Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
	private void getZoomedFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
		double fov = cir.getReturnValue();
		double zoomedFov = fov;

		if (Uniformity.zoom.transitionActive) {
			zoomedFov = Uniformity.zoom.applyZoom(zoomedFov, tickDelta);
		}

		if (fov != zoomedFov) {
			cir.setReturnValue(zoomedFov);
		}
	}
}
