package dev.paw.uniformity.mixins.events;

import com.mojang.authlib.GameProfile;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientPlayerEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.JumpingMount;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Shadow public abstract JumpingMount getJumpingMount();

    private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "swingHand", at = @At("HEAD"), cancellable = true)
    private void onSwingHand(Hand hand, CallbackInfo ci) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnSwingHand(hand)).isCancelled()) ci.cancel();
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        Uniformity.bus.post(new ClientPlayerEvent.OnTickMovement());
    }


    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private void onDropSelectedItem(CallbackInfoReturnable<Boolean> cir) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnDropItem()).isCancelled()) cir.setReturnValue(false);
    }

    @Inject(method = "updateHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getHealth()F", shift = At.Shift.AFTER, ordinal = 0))
    private void onUpdateHealth(float health, CallbackInfo ci) {
        Uniformity.bus.post(new ClientPlayerEvent.OnHealthChange(getHealth(), getHealth() - health));
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasVehicle()Z", ordinal = 0))
    private boolean onHasVehicle(ClientPlayerEntity player) {
        return !Uniformity.bus.post(new ClientPlayerEvent.OnHasVehicle(hasVehicle())).isCancelled() && hasVehicle();
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getJumpingMount()Lnet/minecraft/entity/JumpingMount;", ordinal = 0))
    private JumpingMount onGetJumpingMount(ClientPlayerEntity player) {
        return Uniformity.bus.post(new ClientPlayerEvent.OnGetJumpingMount(getJumpingMount())).isCancelled() ? null : getJumpingMount();
    }

    @Inject(method = "tickRiding", at = @At("HEAD"), cancellable = true)
    private void onTickRiding(CallbackInfo ci) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnTickRiding()).isCancelled()) {
            super.tickRiding();
            ci.cancel();
        }
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void onMove(CallbackInfo ci) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnMove()).isCancelled()) ci.cancel();
    }

    @Inject(method = "isCamera", at = @At("RETURN"), cancellable = true)
    private void onIsCamera(CallbackInfoReturnable<Boolean> cir) {
        if (Uniformity.bus.post(new ClientPlayerEvent.OnIsCamera()).isCancelled()) cir.setReturnValue(false);
    }

    @Inject(method = "isSneaking", at = @At("RETURN"), cancellable = true)
    private void onIsSneaking(CallbackInfoReturnable<Boolean> cir)	{
        cir.setReturnValue(Uniformity.bus.post(new ClientPlayerEvent.OnIsSneaking(cir.getReturnValue())).isSneaking());
    }

    @Override
    public void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
        if (!Uniformity.bus.post(new ClientPlayerEvent.OnChangeLookDirection(cursorDeltaX, cursorDeltaY)).isCancelled()) super.changeLookDirection(cursorDeltaX, cursorDeltaY);
    }
}
