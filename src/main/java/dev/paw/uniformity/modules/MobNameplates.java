package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.EntityHasLabelEvent;
import dev.paw.uniformity.events.EntityRenderEvent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.dizitart.jbus.Subscribe;
import org.joml.Matrix4f;

import java.text.DecimalFormat;

public class MobNameplates extends Module {
    private final DecimalFormat healthFormat = new DecimalFormat("#.##");

    public MobNameplates() {
        super("MobNameplates");
    }

    @Subscribe
    public void renderIfPresent(EntityHasLabelEvent evt) {
        if (isEnabled()) evt.setHasLabel(false);
    }

    @Subscribe
    public void render(EntityRenderEvent evt) {
        if (mc.player == null || mc.world == null || !isEnabled()) return;
        if (!(evt.entity instanceof LivingEntity livingEntity) || !mc.player.canSee(livingEntity) || mc.player.distanceTo(livingEntity) >= 4096) return;
        if (livingEntity == mc.player && !Uniformity.config.freecamToggle) return;
        if (livingEntity.hasPassengers() || livingEntity.isInvisible() || evt.entity instanceof ArmorStandEntity) return;
        if (mc.options.hudHidden) return;

        String name = livingEntity.hasCustomName() ? Formatting.ITALIC + (livingEntity.getCustomName() == null ? "Unknown Name" : livingEntity.getCustomName().getString()) : (livingEntity.getDisplayName() == null ? "Unknown Name" : livingEntity.getDisplayName().getString());
        float nameLen = mc.textRenderer.getWidth(name) * 0.5F;
        float halfSize = nameLen / 2.0F + 15.0F;

        evt.matrixStack.push();
        evt.matrixStack.translate(0,livingEntity.getStandingEyeHeight() + .7, 0);
        evt.matrixStack.multiply(evt.quaternion);
        evt.matrixStack.push();
        evt.matrixStack.scale(-0.0267F, -0.0267F, 0.0267F);
        Matrix4f matrix = evt.matrixStack.peek().getPositionMatrix();

        VertexConsumer builder = evt.vertexConsumers.getBuffer(RenderLayer.getTextBackgroundSeeThrough());
        builder.vertex(matrix, -halfSize - 2, -6, 0.01F).color(0, 0, 0, 64).light(0xF000F0).next();
        builder.vertex(matrix, -halfSize - 2, 6 + 2, 0.01F).color(0, 0, 0, 64).light(0xF000F0).next();
        builder.vertex(matrix, halfSize + 2, 6 + 2, 0.01F).color(0, 0, 0, 64).light(0xF000F0).next();
        builder.vertex(matrix, halfSize + 2, -6, 0.01F).color(0, 0, 0, 64).light(0xF000F0).next();

        int argb = getColor(livingEntity);
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        float maxHealth = Math.max(livingEntity.getHealth(), livingEntity.getMaxHealth());
        float healthHalfSize = halfSize * (livingEntity.getHealth() / maxHealth);

        builder.vertex(matrix, -halfSize, 0, 0.001F).color(r, g, b, 127).light(0xF000F0).next();
        builder.vertex(matrix, -halfSize, 4, 0.001F).color(r, g, b, 127).light(0xF000F0).next();
        builder.vertex(matrix, -halfSize + 2 * healthHalfSize, 4, 0.001F).color(r, g, b, 127).light(0xF000F0).next();
        builder.vertex(matrix, -halfSize + 2 * healthHalfSize, 0, 0.001F).color(r, g, b, 127).light(0xF000F0).next();

        if (healthHalfSize < halfSize) {
            builder.vertex(matrix, -halfSize + 2 * healthHalfSize, 0, 0.001F).color(0, 0, 0, 127).light(0xF000F0).next();
            builder.vertex(matrix, -halfSize + 2 * healthHalfSize, 4, 0.001F).color(0, 0, 0, 127).light(0xF000F0).next();
            builder.vertex(matrix, halfSize, 4, 0.001F).color(0, 0, 0, 127).light(0xF000F0).next();
            builder.vertex(matrix, halfSize, 0, 0.001F).color(0, 0, 0, 127).light(0xF000F0).next();
        }

        evt.matrixStack.push();
        matrix.translate(-halfSize, -4.5F, 0F);
        matrix.scale(0.5F, 0.5F, 0.5F);
        mc.textRenderer.draw(name, 0, 0, 0xFFFFFF, false, matrix, evt.vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        evt.matrixStack.pop();

        final float healthValueTextScale = 0.75F * 0.5F;
        evt.matrixStack.push();
        matrix.scale(2, 2, 2);
        matrix.scale(healthValueTextScale, healthValueTextScale, healthValueTextScale);

        mc.textRenderer.draw(healthFormat.format(livingEntity.getHealth()), 2, 14, 0xFFFFFF, false, matrix, evt.vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        String maxHpStr = Formatting.BOLD + healthFormat.format(livingEntity.getMaxHealth());
        mc.textRenderer.draw(maxHpStr, (int) (halfSize / healthValueTextScale * 2) - mc.textRenderer.getWidth(maxHpStr) - 2, 14, 0xFFFFFF, false, matrix, evt.vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        String percStr = (int) (100 * livingEntity.getHealth() / livingEntity.getMaxHealth()) + "%";
        mc.textRenderer.draw(percStr, (int) (halfSize / healthValueTextScale) - mc.textRenderer.getWidth(percStr) / 2.0F, 14, 0xFFFFFF, false, matrix, evt.vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
        evt.matrixStack.pop();

        evt.matrixStack.pop();
        evt.matrixStack.pop();
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
