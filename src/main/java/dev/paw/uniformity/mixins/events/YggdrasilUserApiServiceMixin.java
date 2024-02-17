package dev.paw.uniformity.mixins.events;

import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;
import com.mojang.authlib.yggdrasil.response.UserAttributesResponse;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.TelemetryEvent;
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
        if (Uniformity.bus.post(new TelemetryEvent()).isCancelled()) cir.setReturnValue(TelemetrySession.DISABLED);
    }

    @Redirect(method = "fetchProperties", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;getTelemetry()Z"), remap = false)
    private boolean getTelemetry(UserAttributesResponse.Privileges privileges) {
        return !Uniformity.bus.post(new TelemetryEvent()).isCancelled() && privileges.getTelemetry();
    }
}
