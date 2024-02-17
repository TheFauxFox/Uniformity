package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.DisconnectScreenEvent;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Shadow @Final private Screen parent;
    @Shadow @Final private DirectionalLayoutWidget grid;

    @Inject(method = "init()V", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        Uniformity.bus.post(new DisconnectScreenEvent(parent, grid, this::addDrawableChild));
    }

    @Override
    public void tick() {
        Uniformity.bus.post(new DisconnectScreenEvent.Tick(parent));
    }
}