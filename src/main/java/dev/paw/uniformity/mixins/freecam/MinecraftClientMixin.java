package dev.paw.uniformity.mixins.freecam;

import dev.paw.uniformity.Uniformity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

@Mixin(MinecraftClient.class)
abstract class MinecraftClientMixin {
	@Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
	private void onDoAttack(CallbackInfoReturnable<Boolean> info) {
		if(Uniformity.config.freecamToggle) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
	private void onHandleBlockBreaking(boolean bl, CallbackInfo info) {
		if(Uniformity.config.freecamToggle) {
			info.cancel();
		}
	}

	@Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
	private void onDoItemUseBefore(CallbackInfo info) {
		if(Uniformity.config.freecamToggle) {
			info.cancel();
		}
	}

	@Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
	private void onHasOutline(Entity entity, CallbackInfoReturnable<Boolean> info) {
		if((Uniformity.config.freecam.highlightPlayer && Uniformity.config.freecamToggle && entity.equals(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().options.hudHidden)) {
			info.setReturnValue(true);
		}
	}

	@Inject(method = "onDisconnected", at = @At("HEAD"))
	private void onDisconnect(CallbackInfo ci) {
		Uniformity.config.freecamToggle = false;
	}
}
