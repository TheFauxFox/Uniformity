package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientPlayerEvent;
import dev.paw.uniformity.events.GameRenderTickEvent;
import dev.paw.uniformity.events.GetFovEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {
    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void removeHandRendering(CallbackInfo ci) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnRenderHand()).isCancelled()) ci.cancel();
    }

    @Redirect(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageTiltYaw()F"))
    public float changeHurtCamType(LivingEntity instance) {
        return Uniformity.bus.post(new ClientPlayerEvent.OnDamageTilt()).isCancelled() ? 0 : instance.getDamageTiltYaw();
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void tickInstances(CallbackInfo info) {
        Uniformity.bus.post(new GameRenderTickEvent());
    }

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    private void getZoomedFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(Uniformity.bus.post(new GetFovEvent(camera, tickDelta, changingFov, cir.getReturnValue())).getFov());
    }
}
