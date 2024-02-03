package dev.paw.uniformity.mixins.recycler;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.ReCycler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V"))
    private void onMouseClick(long window, int button, int action, int mods, CallbackInfo ci) {
        ReCycler rc = Uniformity.getModule(ReCycler.class);
        if (rc != null && MinecraftClient.getInstance().options.useKey.matchesMouse(button) && rc.isEnabled()) {
            rc.useButton = true;
        }
    }
}
