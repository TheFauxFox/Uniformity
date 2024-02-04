package dev.paw.uniformity.mixins.autorefill;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.AutoRefill;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"))
    private void onItemUsePRE(CallbackInfo ci) {
        AutoRefill ar = Uniformity.getModule(AutoRefill.class);
        if (ar != null && ar.isEnabled()) {
            ar.onItemUsePRE();
        }
    }

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onItemUsePOST(CallbackInfo ci) {
        AutoRefill ar = Uniformity.getModule(AutoRefill.class);
        if (ar != null && ar.isEnabled()) {
            ar.onItemUsePOST();
        }
    }
}
