package dev.paw.uniformity.mixins.accessors;

import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractHorseEntity.class)
public interface IA_AbstractHorseEntity {
    @Accessor
    float getJumpStrength();
}
