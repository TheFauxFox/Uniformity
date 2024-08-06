package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.EntityHasLabelEvent;
import dev.paw.uniformity.events.RenderEntityNameEvent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void onRenderLabel(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        RenderEntityNameEvent rene = new RenderEntityNameEvent(entity, text, matrices, vertexConsumers, light);
        if (Uniformity.bus.post(rene).isCancelled()) ci.cancel();
        text = rene.getName();
    }

    @Inject(method = "hasLabel", at = @At("RETURN"), cancellable = true)
    private void onHasLabel(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Uniformity.bus.post(new EntityHasLabelEvent(entity, cir.getReturnValue())).hasLabel());
    }
}
