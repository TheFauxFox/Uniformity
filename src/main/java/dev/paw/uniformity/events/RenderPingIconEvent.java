package dev.paw.uniformity.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public class RenderPingIconEvent extends CancellableEvent {
    public static int WidthOverride = 0;

    public final DrawContext ctx;
    public final int width;
    public final int x;
    public final int y;
    public final PlayerListEntry entry;
    public RenderPingIconEvent(DrawContext context, int width, int x, int y, PlayerListEntry entry) {
        this.ctx = context;
        this.width = width;
        this.x = x;
        this.y = y;
        this.entry = entry;
    }
}
