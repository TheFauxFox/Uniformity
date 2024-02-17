package dev.paw.uniformity.events;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Quaternionf;

public class EntityRenderEvent extends Event {
    public final Entity entity;
    public final MatrixStack matrixStack;
    public final VertexConsumerProvider vertexConsumers;
    public final Quaternionf quaternion;
    public EntityRenderEvent(Entity entity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Quaternionf quaternion) {
        this.entity = entity;
        this.matrixStack = matrixStack;
        this.vertexConsumers = vertexConsumerProvider;
        this.quaternion = quaternion;
    }
}
