package dev.paw.uniformity.events;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

public class RenderEntityNameEvent extends CancellableEvent {
    public final Entity entity;
    public final MatrixStack matrixStack;
    public final VertexConsumerProvider vertexConsumers;
    public final int light;
    private Text name;
    public RenderEntityNameEvent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        this.entity = entity;
        this.name = text;
        this.matrixStack = matrices;
        this.vertexConsumers = vertexConsumers;
        this.light = light;
    }

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }
}
