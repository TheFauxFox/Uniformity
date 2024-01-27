package dev.paw.uniformity.mixins.entityoutline;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Final public GameOptions options;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void onHasOutline(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if(Uniformity.config.entityOutlineToggle && !this.options.hudHidden) {
            if (entity.equals(this.player)) {
                return;
            }

            if (entity instanceof Monster && Uniformity.config.entityOutline.mobHighlight) {
                info.setReturnValue(true);
            }

            if ((entity instanceof PassiveEntity || entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterCreatureEntity) && Uniformity.config.entityOutline.animalHighlight) {
                info.setReturnValue(true);
            }

            if (entity instanceof Angerable && Uniformity.config.entityOutline.angerableHighlight) {
                info.setReturnValue(true);
            }

            if (entity instanceof PlayerEntity && Uniformity.config.entityOutline.playerHighlight) {
                info.setReturnValue(true);
            }
        }
    }
}
