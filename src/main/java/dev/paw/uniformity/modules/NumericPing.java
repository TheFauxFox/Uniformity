package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public class NumericPing extends Module {
    public NumericPing() {
        super("NumericPing");
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.numericPingToggle;
    }

    @Override
    public void setEnabled(boolean value) {

    }

    public void renderPing(int x, int y, int width, DrawContext context, PlayerListEntry entry) {
        Color color;
        int latency = entry.getLatency();

        if (latency < 0) {
            color = Color.BLACK;
        } else if (latency < 150) {
            color = Color.hex(Uniformity.config.numericPing.lowPingColor);
        } else if (latency < 300) {
            color = Color.hex(Uniformity.config.numericPing.lowMidPingColor);
        } else if (latency < 600) {
            color = Color.hex(Uniformity.config.numericPing.midPingColor);
        } else if (latency < 1000) {
            color = Color.hex(Uniformity.config.numericPing.highPingColor);
        } else {
            color = Color.hex(Uniformity.config.numericPing.extremePingColor);
        }

        String ping = latency + "ms";
        int pingWidth = MinecraftClient.getInstance().textRenderer.getWidth(ping);
        context.drawText(MinecraftClient.getInstance().textRenderer, latency + "ms", x + width - pingWidth, y, color.asInt, true);
    }
}
