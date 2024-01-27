package dev.paw.uniformity.mixins.freecam;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
abstract class WorldRendererMixin {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I", ordinal = 0))
	private int onPlayerGlow(Entity entity)	{
		if(entity.equals(MinecraftClient.getInstance().player) && Uniformity.config.freecamToggle && Uniformity.config.freecam.highlightPlayer) {
			return 65280;
		}

		return entity.getTeamColorValue();
	}
}
