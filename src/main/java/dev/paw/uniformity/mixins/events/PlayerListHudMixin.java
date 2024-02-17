package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.RenderPingIconEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Unique boolean wasCancelled = false;

    @ModifyConstant(method = "render", constant = @Constant(intValue = 13))
    public int overridePingWidth(int v) {
        return v + RenderPingIconEvent.WidthOverride;
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    public void onRenderLatency(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        if (Uniformity.bus.post(new RenderPingIconEvent(context, width, x, y, entry)).isCancelled()) {
            wasCancelled = true;
            ci.cancel();
        } else {
            wasCancelled = false;
        }
    }
}
