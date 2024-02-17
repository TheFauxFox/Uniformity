package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.EntityOutlineEvent;
import dev.paw.uniformity.events.TeamColorEvent;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.dizitart.jbus.Subscribe;
import org.lwjgl.glfw.GLFW;

public class EntityOutline extends KeyboundModule {
    public EntityOutline() {
        super("EntityOutline", GLFW.GLFW_KEY_Y);
    }

    @Subscribe
    public void onEntityColor(TeamColorEvent evt) {
        Entity entity = evt.entity;
        if (!isEnabled()) return;

        if (entity.equals(MinecraftClient.getInstance().player)) {
            return;
        }

        if (entity instanceof Monster && Uniformity.config.entityOutline.mobHighlight) {
            evt.setColor(Color.hex(Uniformity.config.entityOutline.mobHighlightHex).asInt);
            return;
        }

        if ((entity instanceof PassiveEntity || entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity) && Uniformity.config.entityOutline.animalHighlight) {
            evt.setColor(Color.hex(Uniformity.config.entityOutline.animalHighlightHex).asInt);
            return;
        }

        if (entity instanceof Angerable && Uniformity.config.entityOutline.angerableHighlight) {
            evt.setColor(Color.hex(Uniformity.config.entityOutline.angerableHighlightHex).asInt);
            return;
        }

        if (entity instanceof PlayerEntity && Uniformity.config.entityOutline.playerHighlight) {
            evt.setColor(Color.hex(Uniformity.config.entityOutline.playerHighlightHex).asInt);
        }
    }

    @Subscribe
    public void onOutline(EntityOutlineEvent evt) {
        if(Uniformity.config.entityOutlineToggle && !mc.options.hudHidden) {
            Entity entity = evt.entity;
            if (entity.equals(mc.player)) {
                return;
            }

            if (entity instanceof Monster && Uniformity.config.entityOutline.mobHighlight) {
                evt.setOutline(true);
            }

            if ((entity instanceof PassiveEntity || entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity) && Uniformity.config.entityOutline.animalHighlight) {
                evt.setOutline(true);
            }

            if (entity instanceof Angerable && Uniformity.config.entityOutline.angerableHighlight) {
                evt.setOutline(true);
            }

            if (entity instanceof PlayerEntity && Uniformity.config.entityOutline.playerHighlight) {
                evt.setOutline(true);
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.entityOutlineToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.entityOutlineToggle = value;
    }
}
