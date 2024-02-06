package dev.paw.uniformity.mixins.fpsdisplay;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, float f, CallbackInfo info) {
        if(this.client.getDebugHud().shouldShowDebugHud() || this.client.options.hudHidden) {
            return;
        }

        if (Uniformity.config.fpsToggle) {
            int x = 1;
            int y = 1;
            context.getMatrices().push();
            context.drawTextWithShadow(client.textRenderer, client.getCurrentFps() + " FPS", x, y, Color.WHITE.asInt);
            context.getMatrices().pop();
        }
    }
}
