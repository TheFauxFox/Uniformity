package dev.paw.uniformity.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
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
}
