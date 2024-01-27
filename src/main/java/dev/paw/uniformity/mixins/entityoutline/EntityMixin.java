package dev.paw.uniformity.mixins.entityoutline;

import dev.paw.uniformity.Uniformity;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract int getId();

    @Inject(method = "getTeamColorValue", at = @At("RETURN"), cancellable = true)
    private void onGetTeamColorValue(CallbackInfoReturnable<Integer> cir) {
        if (MinecraftClient.getInstance().world == null) return;
        Entity entity = MinecraftClient.getInstance().world.getEntityById(getId());
        if (entity == null) return;

        if (entity.equals(MinecraftClient.getInstance().player)) {
            return;
        }

        if (entity instanceof Monster && Uniformity.config.entityOutline.mobHighlight) {
            cir.setReturnValue(Color.hex(Uniformity.config.entityOutline.mobHighlightHex).asInt);
            return;
        }

        if ((entity instanceof PassiveEntity || entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity) && Uniformity.config.entityOutline.animalHighlight) {
            cir.setReturnValue(Color.hex(Uniformity.config.entityOutline.animalHighlightHex).asInt);
            return;
        }

        if (entity instanceof Angerable && Uniformity.config.entityOutline.angerableHighlight) {
            cir.setReturnValue(Color.hex(Uniformity.config.entityOutline.angerableHighlightHex).asInt);
            return;
        }

        if (entity instanceof PlayerEntity && Uniformity.config.entityOutline.playerHighlight) {
            cir.setReturnValue(Color.hex(Uniformity.config.entityOutline.playerHighlightHex).asInt);
            return;
        }
    }
}
