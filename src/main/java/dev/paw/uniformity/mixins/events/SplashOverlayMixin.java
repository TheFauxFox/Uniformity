package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.SplashColorEvent;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SplashOverlay.class)
public class SplashOverlayMixin {
    @SuppressWarnings("unused")
    @Final @Shadow private static final int MOJANG_RED = Uniformity.bus.post(new SplashColorEvent()).getColor();
}
