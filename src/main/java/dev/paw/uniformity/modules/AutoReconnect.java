package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.DisconnectEvent;
import dev.paw.uniformity.events.DisconnectScreenEvent;
import dev.paw.uniformity.utils.Number;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.dizitart.jbus.Subscribe;

import java.util.stream.Stream;

public class AutoReconnect extends Module {
    private static ServerInfo lastServer;
    private int autoReconnectTimer;
    private ButtonWidget autoReconnectButton;
    private Screen parent;

    public AutoReconnect() {
        super("AutoReconnect");
    }

    public static void reconnect(Screen prevScreen) {
        if (lastServer == null) {
            return;
        }

        ConnectScreen.connect(prevScreen, MinecraftClient.getInstance(), ServerAddress.parse(lastServer.address), lastServer, false);
    }

    public static int getWaitTicks() {
        return (Uniformity.config.autoReconnect.reconnectSeconds + (Uniformity.config.autoReconnect.timeJitter ? Number.randInt(-Uniformity.config.autoReconnect.timeJitterSeconds, Uniformity.config.autoReconnect.timeJitterSeconds) : 0)) * 20;
    }

    @Subscribe
    public void onDisconnectScreen(DisconnectScreenEvent evt) {
        if (mc.world == null || mc.world.isClient()) return;
        parent = evt.parent;
        ButtonWidget reconnectButton = evt.grid.add(
                ButtonWidget.builder(
                        Text.translatable("dev.paw.uniformity.reconnect_button"),
                        b -> AutoReconnect.reconnect(parent)
                ).build(),
                evt.grid.copyPositioner().margin(2).marginTop(-6)
        );

        autoReconnectButton = evt.grid.add(
                ButtonWidget.builder(
                        Text.translatable("dev.paw.uniformity.auto_reconnect_button"),
                        b -> {
                            if (toggle()) {
                                autoReconnectTimer = AutoReconnect.getWaitTicks();
                            }
                        }
                ).build(),
                evt.grid.copyPositioner().margin(2)
        );

        evt.grid.refreshPositions();
        Stream.of(reconnectButton, autoReconnectButton).forEach(evt.cb::addDrawableChild);

        if (isEnabled()) {
            autoReconnectTimer = AutoReconnect.getWaitTicks();
        }
    }

    @Subscribe
    public void onScreenTick(DisconnectScreenEvent.Tick evt) {
        if(autoReconnectButton == null) {
            return;
        }

        if(!isEnabled()) {
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

    @Subscribe
    public void onServerDisconnect(DisconnectEvent evt) {
        if (mc.getNetworkHandler() == null) return;
        lastServer = mc.getNetworkHandler().getServerInfo();
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.autoRecconnectToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.autoRecconnectToggle = value;
    }
}
