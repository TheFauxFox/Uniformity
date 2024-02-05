package dev.paw.uniformity.mixins.chatreporttoast;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Redirect(method = "onServerMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;add(Lnet/minecraft/client/toast/Toast;)V"))
    public void onAddChatReportToast(ToastManager instance, Toast toast) {
        if (!Uniformity.config.noChatReportToastToggle) {
            instance.add(toast);
        }
    }
}
