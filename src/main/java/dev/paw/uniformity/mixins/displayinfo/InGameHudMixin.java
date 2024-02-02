package dev.paw.uniformity.mixins.displayinfo;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.Freecam;
import dev.paw.uniformity.utils.Color;
import dev.paw.uniformity.utils.Number;
import dev.paw.uniformity.utils.Str;
import dev.paw.uniformity.utils.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique private final Timer speedTimer = new Timer();
    @Unique private double lastSpeed = 0;
    @Unique private Vec3d lastPos;


    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, float f, CallbackInfo info) {
        if(this.client.getDebugHud().shouldShowDebugHud() || this.client.options.hudHidden) {
            return;
        }

        if (Uniformity.config.displayInfoToggle && client.player != null && client.world != null) {
            int x = 1;
            int y = Uniformity.config.fpsToggle ? 12 : 1;
            context.getMatrices().push();
            context.getMatrices().translate(x, y, 0);
            context.getMatrices().scale(1, 1, 1);
            context.getMatrices().translate(-x, -y, 0);

            if (Uniformity.config.displayInfo.speed) {
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.speedDisplay", Number.round(getSpeed(),2)), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.coords) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                double px = fc != null && fc.isEnabled() ? fc.x : client.player.getX();
                double py = fc != null && fc.isEnabled() ? fc.y : client.player.getY();
                double pz = fc != null && fc.isEnabled() ? fc.z : client.player.getZ();
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionX", Number.round(px,2)), x, y, Color.WHITE.asInt);
                y += 11;
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionY", Number.round(py,2)), x, y, Color.WHITE.asInt);
                y += 11;
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionZ", Number.round(pz,2)), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.netherCoords) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                double px = fc != null && fc.isEnabled() ? fc.x : client.player.getX();
                double pz = fc != null && fc.isEnabled() ? fc.z : client.player.getZ();
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.netherPosTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.netherPosX", Number.round(px / 8,2)), x, y, Color.WHITE.asInt);
                y += 11;
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.netherPosZ", Number.round(pz / 8,2)), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.biome) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                Optional<RegistryKey<Biome>> biome = client.world.getBiome(fc != null && fc.isEnabled() ? BlockPos.ofFloored(fc.x, fc.y, fc.z) : client.player.getBlockPos()).getKey();
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.biomeTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.biome", biome.map(biomeRegistryKey -> Str.titleIdentifier(biomeRegistryKey.getValue().toString())).orElse("Unknown")), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.lightLevel || Uniformity.config.displayInfo.blockLevel) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                int blockL = client.world.getLightLevel(fc != null && fc.isEnabled() ? BlockPos.ofFloored(fc.x, fc.y, fc.z) : client.player.getBlockPos(), 15);
                int skyL = client.world.getLightLevel(fc != null && fc.isEnabled() ? BlockPos.ofFloored(fc.x, fc.y, fc.z) : client.player.getBlockPos());
                context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.lightLevelTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                if (Uniformity.config.displayInfo.blockLevel) {
                    context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.lightBlock", blockL), x, y, Color.WHITE.asInt);
                    y += 11;
                }
                if (Uniformity.config.displayInfo.lightLevel) {
                    context.drawTextWithShadow(client.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.lightSky", skyL), x, y, Color.WHITE.asInt);
                    y += 11;
                }
            }
            context.getMatrices().pop();
        }
    }

    @Unique
    private double getSpeed() {
        if (client.player == null) {
            return lastSpeed;
        }
        if (lastPos == null) {
            lastPos = client.player.getPos().multiply(1,0,1);
        }
        if (speedTimer.hasElapsed(50) && client.player.getPos().distanceTo(lastPos) != 0) {
            lastSpeed = client.player.getPos().multiply(1,0,1).distanceTo(lastPos) / 0.05D;
            lastPos = client.player.getPos().multiply(1,0,1);
            speedTimer.reset();
        }
        return lastSpeed;
    }

}
