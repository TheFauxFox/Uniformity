package dev.paw.uniformity.mixins.notoolbreak;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.AntiToolBreak;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    public void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        AntiToolBreak tb = Uniformity.getModule(AntiToolBreak.class);
        if (tb != null && tb.isEnabled() && tb.shouldCancelToolUse(false)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    public void onHandleBlockBreaking(boolean breaking, CallbackInfo ci) {
        AntiToolBreak tb = Uniformity.getModule(AntiToolBreak.class);
        if (tb != null && tb.isEnabled() && tb.shouldCancelToolUse(false)) {
            ci.cancel();
        }
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    public void onDoItemUse(CallbackInfo ci) {
        AntiToolBreak tb = Uniformity.getModule(AntiToolBreak.class);
        if (tb != null && tb.isEnabled() && tb.shouldCancelToolUse(true)) {
            ci.cancel();
        }
    }
}
