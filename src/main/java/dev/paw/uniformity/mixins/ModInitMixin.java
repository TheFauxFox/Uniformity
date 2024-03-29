package dev.paw.uniformity.mixins;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ModInitMixin {
    @Inject(method = "init", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        if (!Uniformity.isInit) Uniformity.onClientInit();
    }
}
