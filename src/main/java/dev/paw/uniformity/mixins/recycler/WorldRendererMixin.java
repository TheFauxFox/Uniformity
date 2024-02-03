package dev.paw.uniformity.mixins.recycler;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.ReCycler;
import dev.paw.uniformity.utils.Color;
import dev.paw.uniformity.utils.Render3D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void postWorldRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ReCycler rc = Uniformity.getModule(ReCycler.class);

        if (mc.player == null || mc.world == null || rc == null || !rc.isEnabled()) return;

        Render3D.begin(matrices);

        if (rc.villager != null) {
            matrices.push();
            Render3D.drawOutlinedBox(rc.villager.getBoundingBox(), matrices, Color.rgba(200, 200, 200, 128));
            Render3D.drawSolidBox(rc.villager.getBoundingBox(), matrices, Color.rgba(85,85,85, 70));
            matrices.pop();
        }

        if (rc.lecternPos != null) {
            matrices.push();
            Render3D.drawOutlinedBox(new Box(rc.lecternPos), matrices, Color.rgba(200, 200, 200, 128));
            Render3D.drawSolidBox(new Box(rc.lecternPos), matrices, Color.rgba(85,85,85, 70));
            matrices.pop();
        }

        Render3D.end(matrices);
    }
}
