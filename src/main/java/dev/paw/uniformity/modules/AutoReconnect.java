package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.utils.Number;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class AutoReconnect {
    private static ServerInfo lastServer;

    public static void setLastServer(ServerInfo server) {
        lastServer = server;
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
}
