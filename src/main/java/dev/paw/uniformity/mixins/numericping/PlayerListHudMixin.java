package dev.paw.uniformity.mixins.numericping;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.NumericPing;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @ModifyConstant(method = "render", constant = @Constant(intValue = 13))
    public int overridePingWidth(int v) {
        return v + (Uniformity.config.numericPingToggle ? Uniformity.config.numericPing.pingWidthOverride : 0);
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    public void onRenderLatency(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        NumericPing np = Uniformity.getModule(NumericPing.class);
        if (np != null && np.isEnabled()) {
            np.renderPing(x, y, width, context, entry);
            ci.cancel();
        }
    }
}
