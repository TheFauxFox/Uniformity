package dev.paw.uniformity.mixins.autoreconnect;

import dev.paw.uniformity.modules.AutoReconnect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public abstract ClientPlayNetworkHandler getNetworkHandler();

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void onDisconnect(Screen disconnectionScreen, CallbackInfo ci) {
        AutoReconnect.setLastServer(getNetworkHandler() == null ? null : getNetworkHandler().getServerInfo());
    }
}
