package dev.paw.uniformity.mixins.autoreconnect;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.AutoReconnect;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private int autoReconnectTimer;
    @Unique
    private ButtonWidget autoReconnectButton;

    @Shadow @Final private Screen parent;
    @Shadow @Final private DirectionalLayoutWidget grid;

    @Inject(at = @At("TAIL"), method = "init()V")
    private void onInit(CallbackInfo ci) {
        addReconnectButtons();
    }

    @Unique
    private void addReconnectButtons() {
        ButtonWidget reconnectButton = grid.add(
                ButtonWidget.builder(
                        Text.translatable("dev.paw.uniformity.reconnect_button"),
                        b -> AutoReconnect.reconnect(parent)
                ).build(),
                grid.copyPositioner().margin(2).marginTop(-6)
        );

        autoReconnectButton = grid.add(
                ButtonWidget.builder(
                        Text.translatable("dev.paw.uniformity.auto_reconnect_button"),
                        b -> pressAutoReconnect()
                ).build(),
                grid.copyPositioner().margin(2)
        );

        grid.refreshPositions();
        Stream.of(reconnectButton, autoReconnectButton).forEach(this::addDrawableChild);

        if (Uniformity.config.autoRecconnectToggle) {
            autoReconnectTimer = AutoReconnect.getWaitTicks();
        }
    }

    @Unique
    private void pressAutoReconnect() {
        Uniformity.config.autoRecconnectToggle =! Uniformity.config.autoRecconnectToggle;

        if (Uniformity.config.autoRecconnectToggle) {
            autoReconnectTimer = AutoReconnect.getWaitTicks();
        }
    }

    @Override
    public void tick() {
        if(autoReconnectButton == null) {
            return;
        }

        if(!Uniformity.config.autoRecconnectToggle) {
            autoReconnectButton.setMessage(Text.translatable("dev.paw.uniformity.auto_reconnect_button"));
            return;
        }

        autoReconnectButton.setMessage(Text.translatable("dev.paw.uniformity.auto_reconnect_button_countdown", (int)Math.ceil(autoReconnectTimer / 20.0)));

        if(autoReconnectTimer > 0) {
            autoReconnectTimer--;
            return;
        }

        AutoReconnect.reconnect(parent);
    }
}
