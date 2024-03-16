package dev.paw.uniformity.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.MouseButtonEvent;
import dev.paw.uniformity.events.Render3dEvent;
import dev.paw.uniformity.utils.Color;
import dev.paw.uniformity.utils.Render3D;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.dizitart.jbus.Subscribe;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeasuringTape extends KeyboundModule {
    private BlockPos firstPos;
    private final ArrayList<Pair<BlockPos, BlockPos>> boxes = new ArrayList<>();
    private final List<Color> colors = Arrays.asList(
            Color.RED,
            Color.DARK_RED,
            Color.GOLD,
            Color.YELLOW,
            Color.GREEN,
            Color.DARK_GREEN,
            Color.AQUA,
            Color.BLUE,
            Color.LIGHT_PURPLE,
            Color.DARK_PURPLE
    );

    public MeasuringTape() {
        super("MeasuringTape", -1);
    }

    @Subscribe
    public void onRender3d(Render3dEvent evt) {
        Render3D.begin(evt.matrices);

        if (firstPos != null && isHoldingMeasureItem()) {
            Color color = colors.get(boxes.size() % colors.size());
            evt.matrices.push();
            color.setShader();
            Box bb;
            if (getTargetedBlockPos() != null) {
                bb = new Box(firstPos.toCenterPos(), getTargetedBlockPos().toCenterPos()).expand(0.5);
            } else {
                bb = new Box(firstPos.toCenterPos(), firstPos.toCenterPos()).expand(0.5);
            }
            Render3D.drawOutlinedBox(bb, evt.matrices);
            Color.resetShader();
            evt.matrices.pop();

            drawSizeLabels(bb, evt.matrices, evt.camera.getRotation(), color);
        }
        int indx = 0;
        for (Pair<BlockPos, BlockPos> box: boxes) {
            Color color = colors.get(indx);
            evt.matrices.push();
            color.setShader();
            Box bb = new Box(box.getLeft().toCenterPos(), box.getRight().toCenterPos()).expand(0.5);
            Render3D.drawOutlinedBox(bb, evt.matrices);
            Color.resetShader();
            evt.matrices.pop();

            drawSizeLabels(bb, evt.matrices, evt.camera.getRotation(), color);

            indx ++;
            if (indx >= colors.size()) {
                indx = 0;
            }
        }
        Render3D.end(evt.matrices);
    }

    private void drawSizeLabels(Box bb, MatrixStack matrixStack, Quaternionf rotation, Color color) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();


        matrixStack.push();
        matrixStack.translate(bb.maxX - (bb.getLengthX() / 2), bb.maxY + 0.128, bb.maxZ);
        matrixStack.multiply(rotation);
        matrixStack.scale(-0.025f, -0.025f, 1);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        String text = String.valueOf((int)bb.getLengthX());
        int width = mc.textRenderer.getWidth(text);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(buffer);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, -1, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, -1, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        Tessellator.getInstance().draw();
        mc.textRenderer.draw(text, 0, 0, Colors.BLACK, false, matrix4f, immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
        immediate.draw();
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(bb.maxX, bb.maxY + 0.128, bb.maxZ - (bb.getLengthZ() / 2));
        matrixStack.multiply(rotation);
        matrixStack.scale(-0.025f, -0.025f, 1);
        matrix4f = matrixStack.peek().getPositionMatrix();
        text = String.valueOf((int)bb.getLengthZ());
        width = mc.textRenderer.getWidth(text);
        buffer = Tessellator.getInstance().getBuffer();
        immediate = VertexConsumerProvider.immediate(buffer);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, -1, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, -1, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        Tessellator.getInstance().draw();
        mc.textRenderer.draw(text, 0, 0, Colors.BLACK, false, matrix4f, immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
        immediate.draw();
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(bb.maxX, bb.maxY - (bb.getLengthY() / 2) + 0.128, bb.maxZ);
        matrixStack.multiply(rotation);
        matrixStack.scale(-0.025f, -0.025f, 1);
        matrix4f = matrixStack.peek().getPositionMatrix();
        text = String.valueOf((int)bb.getLengthY());
        width = mc.textRenderer.getWidth(text);
        buffer = Tessellator.getInstance().getBuffer();
        immediate = VertexConsumerProvider.immediate(buffer);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, -1, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, -1, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        Tessellator.getInstance().draw();
        mc.textRenderer.draw(text, 0, 0, Colors.BLACK, false, matrix4f, immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
        immediate.draw();
        matrixStack.pop();


        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }

    @Subscribe
    public void onMouseButton(MouseButtonEvent evt) {
        if (!isEnabled() || mc.player == null) return;

        if (mc.options.sneakKey.isPressed() && isHoldingMeasureItem() && mc.options.attackKey.isPressed()) {
            firstPos = null;
            boxes.clear();
            return;
        }

        if (isHoldingMeasureItem() && mc.options.useKey.isPressed() && getTargetedBlockPos() != null) {
            if (firstPos == null) {
                firstPos = getTargetedBlockPos();
            } else  {
                boxes.add(new Pair<>(firstPos, getTargetedBlockPos()));
                firstPos = null;
            }
        }
    }

    private boolean isHoldingMeasureItem() {
        return mc.player != null && mc.player.isHolding(Registries.ITEM.get(new Identifier(Uniformity.config.measuringTape.measuringItem)));
    }

    private BlockPos getTargetedBlockPos() {
        if (mc.crosshairTarget != null && mc.player != null) {
            if (mc.options.sneakKey.isPressed()) {
                Vec3d lookVec = mc.player.getRotationVecClient();
                return BlockPos.ofFloored(mc.crosshairTarget.getPos().add(lookVec.x, lookVec.y, lookVec.z));
            }
            return BlockPos.ofFloored(mc.crosshairTarget.getPos());
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.measuringTapeToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        if (!value) {
            firstPos = null;
            boxes.clear();
        }
        Uniformity.config.measuringTapeToggle = value;
    }
}
