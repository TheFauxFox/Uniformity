package dev.paw.uniformity.mixins.notelemetry;

import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;
import com.mojang.authlib.yggdrasil.response.UserAttributesResponse;
import dev.paw.uniformity.Uniformity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Executor;

@Mixin(YggdrasilUserApiService.class)
public class YggdrasilUserApiServiceMixin {
    @Inject(method = "newTelemetrySession", at = @At("HEAD"), cancellable = true, remap = false)
    private void onNewTelemetrySession(Executor executor, CallbackInfoReturnable<TelemetrySession> cir) {
        if (Uniformity.config.disableTelemetryToggle) cir.setReturnValue(TelemetrySession.DISABLED);
    }

    @Redirect(method = "fetchProperties", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;getTelemetry()Z"), remap = false)
    private boolean getTelemetry(UserAttributesResponse.Privileges privileges) {
        if (Uniformity.config.disableTelemetryToggle) return false;
        return privileges.getTelemetry();
    }
}
