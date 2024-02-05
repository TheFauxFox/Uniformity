package dev.paw.uniformity.mixins.freecam;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.entity.Entity;

@Mixin(PlayerEntityRenderer.class)
abstract class PlayerEntityRendererMixin<T extends Entity> extends EntityRenderer<T> {
	@Override
	protected boolean hasLabel(T entity) {
		if(Uniformity.config.freecamToggle && entity == MinecraftClient.getInstance().player && !MinecraftClient.getInstance().options.hudHidden && !Uniformity.config.mobNameplatesToggle) {
			return true;
		}

		return super.hasLabel(entity);
	}

	protected PlayerEntityRendererMixin(Context ctx) {
		super(ctx);
	}
}
