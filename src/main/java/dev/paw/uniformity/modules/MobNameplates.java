package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.text.DecimalFormat;

public class MobNameplates extends Module {
    public MobNameplates() {
        super("MobNameplates");
    }

    public void render(Entity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Quaternionf cameraRotation) {
        if (mc.player == null || mc.world == null) return;
        if (!(entity instanceof LivingEntity livingEntity) || !mc.player.canSee(livingEntity) || mc.player.distanceTo(livingEntity) >= 4096) return;
        if (livingEntity == mc.player && !Uniformity.config.freecamToggle) return;
        if (mc.options.hudHidden) return;

        DecimalFormat healthFormat = new DecimalFormat("#.##");
        int light = 0xF000F0;
        float globalScale = 0.0267F;
        float textScale = 0.5F;
        String name = livingEntity.hasCustomName() ? Formatting.ITALIC + livingEntity.getCustomName().getString() : livingEntity.getDisplayName().getString();
        float nameLen = mc.textRenderer.getWidth(name) * textScale;
        float halfSize = nameLen / 2.0F + 15.0F;
        float padding = 2;
        int bgHeight = 6;
        int barHeight = 4;
        int white = 0xFFFFFF;
        int black = 0;

        matrices.push();
        matrices.translate(0,livingEntity.getStandingEyeHeight() + .7, 0);
        matrices.multiply(cameraRotation);
        matrices.push();
        matrices.scale(-globalScale, -globalScale, globalScale);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        VertexConsumer builder = vertexConsumers.getBuffer(RenderLayer.getTextBackgroundSeeThrough());
        builder.vertex(matrix, -halfSize - padding, -bgHeight, 0.01F).color(0, 0, 0, 64).light(light).next();
        builder.vertex(matrix, -halfSize - padding, barHeight + padding, 0.01F).color(0, 0, 0, 64).light(light).next();
        builder.vertex(matrix, halfSize + padding, barHeight + padding, 0.01F).color(0, 0, 0, 64).light(light).next();
        builder.vertex(matrix, halfSize + padding, -bgHeight, 0.01F).color(0, 0, 0, 64).light(light).next();

        int argb = getColor(livingEntity);
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        float maxHealth = Math.max(livingEntity.getHealth(), livingEntity.getMaxHealth());
        float healthHalfSize = halfSize * (livingEntity.getHealth() / maxHealth);

        builder.vertex(matrix, -halfSize, 0, 0.001F).color(r, g, b, 127).light(light).next();
        builder.vertex(matrix, -halfSize, barHeight, 0.001F).color(r, g, b, 127).light(light).next();
        builder.vertex(matrix, -halfSize + 2 * healthHalfSize, barHeight, 0.001F).color(r, g, b, 127).light(light).next();
        builder.vertex(matrix, -halfSize + 2 * healthHalfSize, 0, 0.001F).color(r, g, b, 127).light(light).next();

        if (healthHalfSize < halfSize) {
            builder.vertex(matrix, -halfSize + 2 * healthHalfSize, 0, 0.001F).color(0, 0, 0, 127).light(light).next();
            builder.vertex(matrix, -halfSize + 2 * healthHalfSize, barHeight, 0.001F).color(0, 0, 0, 127).light(light).next();
            builder.vertex(matrix, halfSize, barHeight, 0.001F).color(0, 0, 0, 127).light(light).next();
            builder.vertex(matrix, halfSize, 0, 0.001F).color(0, 0, 0, 127).light(light).next();
        }

        matrices.push();
        matrix.translate(-halfSize, -4.5F, 0F);
        matrix.scale(textScale, textScale, textScale);
        mc.textRenderer.draw(name, 0, 0, white, false, matrix, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, black, light);
        matrices.pop();

        final float healthValueTextScale = 0.75F * textScale;
        matrices.push();
        matrix.scale(2, 2, 2);
        matrix.scale(healthValueTextScale, healthValueTextScale, healthValueTextScale);

        String hpStr = healthFormat.format(livingEntity.getHealth());
        mc.textRenderer.draw(hpStr, 2, 14, white, false, matrix, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, black, light);
        String maxHpStr = Formatting.BOLD + healthFormat.format(livingEntity.getMaxHealth());
        mc.textRenderer.draw(maxHpStr, (int) (halfSize / healthValueTextScale * 2) - mc.textRenderer.getWidth(maxHpStr) - 2, 14, white, false, matrix, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, black, light);
        String percStr = (int) (100 * livingEntity.getHealth() / livingEntity.getMaxHealth()) + "%";
        mc.textRenderer.draw(percStr, (int) (halfSize / healthValueTextScale) - mc.textRenderer.getWidth(percStr) / 2.0F, 14, white, false, matrix, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, black, light);
        matrices.pop();

        matrices.pop();
        matrices.pop();
    }

    private int getColor(LivingEntity entity) {
        float health = MathHelper.clamp(entity.getHealth(), 0.0F, entity.getMaxHealth());
        float hue = Math.max(0.0F, (health / entity.getMaxHealth()) / 3.0F - 0.07F);
        return MathHelper.hsvToRgb(hue, 1.0F, 1.0F);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.mobNameplatesToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.mobNameplatesToggle = value;
    }
}
