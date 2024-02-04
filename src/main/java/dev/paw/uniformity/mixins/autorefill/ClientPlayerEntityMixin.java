package dev.paw.uniformity.mixins.autorefill;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.AutoRefill;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "swingHand", at = @At("HEAD"))
    private void onSwingHand(Hand hand, CallbackInfo ci) {
        AutoRefill ar = Uniformity.getModule(AutoRefill.class);
        if (ar != null && ar.isEnabled()) {
            ar.onHandSwing(hand);
        }
    }
}
