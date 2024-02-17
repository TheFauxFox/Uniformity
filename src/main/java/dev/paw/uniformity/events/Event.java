package dev.paw.uniformity.events;

import net.minecraft.client.MinecraftClient;

public abstract class Event {
    public final MinecraftClient client = MinecraftClient.getInstance();
}
