package dev.paw.uniformity.mixins.events;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.MouseButtonEvent;
import dev.paw.uniformity.events.MouseMoveEvent;
import dev.paw.uniformity.events.MouseScrollEvent;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow private double eventDeltaVerticalWheel;

    @Inject(method = "onMouseScroll", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Mouse;eventDeltaVerticalWheel:D", ordinal = 7), cancellable = true)
    private void onMouseScroll(CallbackInfo ci) {
        if (eventDeltaVerticalWheel != 0) {
            if (Uniformity.bus.post(new MouseScrollEvent(eventDeltaVerticalWheel)).isCancelled()) ci.cancel();
        }
    }

    @Inject(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V", shift = At.Shift.BEFORE), cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (Uniformity.bus.post(new MouseButtonEvent(Arrays.stream(MouseButtonEvent.Button.values()).toList().get(button))).isCancelled()) ci.cancel();
    }

    @Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getInvertYMouse()Lnet/minecraft/client/option/SimpleOption;"), cancellable = true)
    public void omMouseMove(CallbackInfo ci, @Local(ordinal = 1) double e, @Local(ordinal = 2) LocalDoubleRef k, @Local(ordinal = 3) LocalDoubleRef l, @Local(ordinal = 6) double h) {
        MouseMoveEvent mme = new MouseMoveEvent(k.get(), l.get());
        Uniformity.bus.post(mme);
        if (mme.isCancelled()) {
            ci.cancel();
        } else {
            k.set(mme.getX());
            l.set(mme.getY());
        }
    }
}
