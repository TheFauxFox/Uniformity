package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        Uniformity.bus.post(new ClientTickEvent());
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void onDisconnect(Screen disconnectionScreen, CallbackInfo ci) {
        Uniformity.bus.post(new DisconnectEvent());
    }

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    private void onItemUsePRE(CallbackInfo ci) {
        if (Uniformity.bus.post(new ClientPlayerEvent.ItemUseEvent.PRE()).isCancelled()) ci.cancel();
    }

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void onItemUsePOST(CallbackInfo ci) {
        if (Uniformity.bus.post(new ClientPlayerEvent.ItemUseEvent.POST()).isCancelled()) ci.cancel();
    }

    @Inject(method = "hasOutline", at = @At("RETURN"), cancellable = true)
    private void onHasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Uniformity.bus.post(new EntityOutlineEvent(entity, cir.getReturnValue())).hasOutline());
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void onDoAttackPRE(CallbackInfoReturnable<Boolean> cir) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnDoAttack.PRE()).isCancelled()) cir.setReturnValue(false);
    }

    @Inject(method = "doAttack", at = @At("RETURN"), cancellable = true)
    private void onDoAttackPOST(CallbackInfoReturnable<Boolean> cir) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnDoAttack.POST()).isCancelled()) cir.setReturnValue(false);
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void onHandleBlockBreaking(boolean bl, CallbackInfo ci) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnHandleBlockBreaking(bl)).isCancelled()) ci.cancel();
    }

    @Inject(method = "isTelemetryEnabledByApi", at = @At("HEAD"), cancellable = true)
    private void onIsTelemetryEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (Uniformity.bus.post(new TelemetryEvent()).isCancelled()) cir.setReturnValue(false);
    }
}
