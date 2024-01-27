package dev.paw.uniformity.mixins.creativetools;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @Inject(method = "shouldShowOperatorTab", at = @At("RETURN"), cancellable = true)
    public void onShouldShowOpTab(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (Uniformity.config.creativeToolsToggle) {
            cir.setReturnValue(true);
        }
    }
}
