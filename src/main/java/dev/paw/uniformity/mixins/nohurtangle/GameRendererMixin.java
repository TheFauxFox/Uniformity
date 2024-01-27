package dev.paw.uniformity.mixins.nohurtangle;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageTiltYaw()F"))
    public float changeHurtCamType(LivingEntity instance) {
        return Uniformity.config.noHurtAngleToggle ? 0 : instance.getDamageTiltYaw();
    }
}
