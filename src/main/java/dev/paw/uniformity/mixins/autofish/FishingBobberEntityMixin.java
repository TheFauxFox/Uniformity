package dev.paw.uniformity.mixins.autofish;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.AutoFish;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {
    @Shadow @Nullable public abstract PlayerEntity getPlayerOwner();

    @Inject(method = "onTrackedDataSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;setVelocity(DDD)V"))
    private void onCaughtFish(TrackedData<?> data, CallbackInfo ci) {
        AutoFish af = Uniformity.getModule(AutoFish.class);
        if (af != null && af.isEnabled() && getPlayerOwner() == MinecraftClient.getInstance().player) {
            af.doAutoFish();
        }
    }
}
