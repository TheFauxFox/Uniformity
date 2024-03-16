package dev.paw.uniformity.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("unused")
public class Render3D {
    public static void applyRenderOffset(MatrixStack matrixStack) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera cam = mc.getBlockEntityRenderDispatcher().camera;
        Vec3d camPos = cam == null ? Vec3d.ZERO : cam.getPos();
        matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
    }

    public static void begin(MatrixStack matrixStack) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        matrixStack.push();
        applyRenderOffset(matrixStack);
    }

    public static void end(MatrixStack matrixStack) {
        matrixStack.pop();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void setColor(float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    public static void setColor(int red, int green, int blue, int alpha) {
        setColor(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
    }

    public static void setColor(int red, int green, int blue) {
        setColor(red, green, blue, 255);
    }

    public static void setColor(Color color) {
        setColor(color.floatRed, color.floatGreen, color.floatBlue, color.floatAlpha);
    }

    public static void drawOutlinedBox(Box bb, MatrixStack matrixStack, Color color) {
        setColor(color);
        drawOutlinedBox(bb, matrixStack);
    }

    public static void drawOutlinedBox(Box bb, MatrixStack matrixStack) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).next();

        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).next();

        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).next();

        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).next();

        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).next();
        tessellator.draw();
    }

    public static void drawSolidBox(Box bb, MatrixStack matrixStack, Color color) {
        setColor(color);
        drawSolidBox(bb, matrixStack);
    }

    public static void drawSolidBox(Box bb, MatrixStack matrixStack) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).next();

        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.maxX, (float)bb.maxY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).next();

        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.minZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.minY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.maxZ).next();
        bufferBuilder.vertex(matrix, (float)bb.minX, (float)bb.maxY, (float)bb.minZ).next();
        tessellator.draw();
    }

    public static void renderTag(String tag, double x, double y, double z, MatrixStack matrix, Quaternionf rotation, Color color) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        String text = String.valueOf(tag);
        int width = textRenderer.getWidth(text);

        matrix.push();
        matrix.translate(x, y, z);
        matrix.multiply(rotation);
        matrix.scale(-0.025f, -0.025f, 1);
        matrix.translate((-width / 2f), 0.0, 0.0);
        Matrix4f matrix4f = matrix.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(buffer);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix4f, -1, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, -1, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, 8, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        buffer.vertex(matrix4f, width, -1, 0).color(color.floatRed, color.floatGreen, color.floatBlue, 0.8f).next();
        float[] components = color.floats();
        float luminance = (0.299f * components[0] + 0.587f * components[1] + 0.114f * components[2]);
        int textColor = luminance < 0.4f ? DyeColor.WHITE.getSignColor() : DyeColor.BLACK.getSignColor();
        Tessellator.getInstance().draw();
        textRenderer.draw(text, 0, 0, textColor, false, matrix4f, immediate, TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
        immediate.draw();
        matrix.pop();

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }
}
