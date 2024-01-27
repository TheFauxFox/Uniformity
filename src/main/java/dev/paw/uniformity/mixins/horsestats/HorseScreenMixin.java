package dev.paw.uniformity.mixins.horsestats;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.HorseStats;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseScreen.class)
public class HorseScreenMixin {
    @Shadow @Final private AbstractHorseEntity entity;

    @Inject(method = "render", at = @At("TAIL"))
    public void drawHorseStats(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        HorseStats hs = Uniformity.getModule(HorseStats.class);
        if (hs != null && hs.isEnabled()) {
            hs.drawHorseStats(context, entity);
        }
    }
}
