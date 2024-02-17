package dev.paw.uniformity.events;

import net.minecraft.entity.player.PlayerEntity;

public class CaughtFishEvent extends Event {
    public final PlayerEntity fisher;

    public CaughtFishEvent(PlayerEntity owner) {
        fisher = owner;
    }
}
