package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.CameraUpdateEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow protected abstract void setPos(double x, double y, double z);

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", shift = At.Shift.BEFORE), cancellable = true)
    private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Uniformity.bus.post(new CameraUpdateEvent(area, focusedEntity, thirdPerson, inverseView, tickDelta, this::setRotation, this::setPos)).isCancelled()) ci.cancel();
    }

    @Inject(method = "isThirdPerson", at = @At("RETURN"), cancellable = true)
    private void onIsThirdPerson(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Uniformity.bus.post(new CameraUpdateEvent.ThirdPerson(cir.getReturnValue())).isThirdPerson());
    }
}
