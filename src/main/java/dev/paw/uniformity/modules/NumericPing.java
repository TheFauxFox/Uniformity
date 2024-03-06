package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.RenderPingIconEvent;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.MinecraftClient;
import org.dizitart.jbus.Subscribe;

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
        Uniformity.config.numericPingToggle = value;
    }

    @Subscribe
    public void renderPing(RenderPingIconEvent evt) {
        if (!isEnabled()) {
            RenderPingIconEvent.WidthOverride = 0;
            return;
        }

        evt.cancel();
        RenderPingIconEvent.WidthOverride = Uniformity.config.numericPing.pingWidthOverride;
        Color color;
        int latency = evt.entry.getLatency();

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
        evt.ctx.drawText(MinecraftClient.getInstance().textRenderer, latency + "ms", evt.x + evt.width - pingWidth, evt.y, color.asInt, true);
    }
}
