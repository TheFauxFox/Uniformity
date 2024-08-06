package dev.paw.uniformity.mixins.accessors;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.BufferAllocator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BufferBuilder.class)
public interface IA_BufferBuilder {
    @Accessor
    BufferAllocator getAllocator();
}
