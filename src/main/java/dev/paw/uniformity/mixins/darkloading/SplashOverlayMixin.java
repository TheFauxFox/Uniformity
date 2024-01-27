package dev.paw.uniformity.mixins.darkloading;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {
    @Final
    @Shadow
    private static final int MOJANG_RED = (
            Uniformity.config.darkLoadingScreenToggle ?
                    Color.hex(Uniformity.config.darkLoadingScreen.loadingScreenHex).asInt :
                    Color.rgb(239, 50, 61).asInt
    );
}
