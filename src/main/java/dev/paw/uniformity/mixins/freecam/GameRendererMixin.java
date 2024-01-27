package dev.paw.uniformity.mixins.freecam;

import dev.paw.uniformity.Uniformity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {
	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
	private void removeHandRendering(CallbackInfo info) {
		if(Uniformity.config.freecamToggle) {
			info.cancel();
		}
	}
}
