package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.Render2dEvent;
import dev.paw.uniformity.utils.Color;
import dev.paw.uniformity.utils.Number;
import dev.paw.uniformity.utils.Str;
import dev.paw.uniformity.utils.Timer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import org.dizitart.jbus.Subscribe;

import java.util.Optional;

public class DisplayInfo extends Module {
    private final Timer speedTimer = new Timer();
    private double lastSpeed = 0;
    private Vec3d lastPos;

    public DisplayInfo() {
        super("DisplayInfo");
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.displayInfoToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.displayInfoToggle = value;
    }

    @Subscribe
    public void render(Render2dEvent evt) {
        if (isEnabled() && mc.player != null && mc.world != null && !evt.hideHud()) {
            int x = 1;
            int y = Uniformity.config.fpsToggle ? 12 : 1;
            evt.ctx.getMatrices().push();
            evt.ctx.getMatrices().translate(x, y, 0);
            evt.ctx.getMatrices().scale(1, 1, 1);
            evt.ctx.getMatrices().translate(-x, -y, 0);

            if (Uniformity.config.displayInfo.speed) {
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.speedDisplay", Number.round(getSpeed(), 2)), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.coords) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                double px = fc != null && fc.isEnabled() ? fc.x : mc.player.getX();
                double py = fc != null && fc.isEnabled() ? fc.y : mc.player.getY();
                double pz = fc != null && fc.isEnabled() ? fc.z : mc.player.getZ();
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionX", Number.round(px, 2)), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionY", Number.round(py, 2)), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.positionZ", Number.round(pz, 2)), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.netherCoords) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                double px = fc != null && fc.isEnabled() ? fc.x : mc.player.getX();
                double pz = fc != null && fc.isEnabled() ? fc.z : mc.player.getZ();
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.netherPosTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.netherPosX", Number.round(px / 8, 2)), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.netherPosZ", Number.round(pz / 8, 2)), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.biome) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                Optional<RegistryKey<Biome>> biome = mc.world.getBiome(fc != null && fc.isEnabled() ? BlockPos.ofFloored(fc.x, fc.y, fc.z) : mc.player.getBlockPos()).getKey();
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.biomeTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.biome", biome.map(biomeRegistryKey -> Str.titleIdentifier(biomeRegistryKey.getValue().toString())).orElse("Unknown")), x, y, Color.WHITE.asInt);
                y += 11;
            }
            if (Uniformity.config.displayInfo.lightLevel || Uniformity.config.displayInfo.blockLevel) {
                Freecam fc = Uniformity.getModule(Freecam.class);
                int blockL = mc.world.getLightLevel(fc != null && fc.isEnabled() ? BlockPos.ofFloored(fc.x, fc.y, fc.z) : mc.player.getBlockPos(), 15);
                int skyL = mc.world.getLightLevel(fc != null && fc.isEnabled() ? BlockPos.ofFloored(fc.x, fc.y, fc.z) : mc.player.getBlockPos());
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.lightLevelTitle"), x, y, Color.WHITE.asInt);
                y += 11;
                if (Uniformity.config.displayInfo.blockLevel) {
                    evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.lightBlock", blockL), x, y, Color.WHITE.asInt);
                    y += 11;
                }
                if (Uniformity.config.displayInfo.lightLevel) {
                    evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.lightSky", skyL), x, y, Color.WHITE.asInt);
                }
                y += 11;
            }
            if (Uniformity.config.recyclerToggle && Uniformity.config.displayInfo.showRecycler) {
                ReCycler rc = Uniformity.getModule(ReCycler.class);
                if (rc == null || !rc.isEnabled()) return;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.recycler.title"), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.recycler.villager", rc.villager == null ? Text.translatable("dev.paw.uniformity.cross") : Text.translatable("dev.paw.uniformity.check")), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.recycler.lecturn", rc.lecternPos == null ? Text.translatable("dev.paw.uniformity.cross") : Text.translatable("dev.paw.uniformity.check")), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.stringifiedTranslatable("dev.paw.uniformity.displayInfo.recycler.enchant", rc.targetEnchant == null ? Text.translatable("dev.paw.uniformity.cross") : Str.getEnchantName(rc.targetEnchant)), x, y, Color.WHITE.asInt);
                y += 11;
                evt.ctx.drawTextWithShadow(mc.textRenderer, Text.stringifiedTranslatable("dev.paw.uniformity.displayInfo.recycler.threshold", rc.threshold == -1 ? Text.translatable("dev.paw.uniformity.cross") : rc.threshold), x, y, Color.WHITE.asInt);
                y += 11;
                if (rc.stepping) {
                    evt.ctx.drawTextWithShadow(mc.textRenderer, Text.translatable("dev.paw.uniformity.displayInfo.recycler.time", rc.timer.getTimeStr()), x, y, Color.WHITE.asInt);
                    y += 11;
                    evt.ctx.drawTextWithShadow(mc.textRenderer, Text.stringifiedTranslatable("dev.paw.uniformity.displayInfo.recycler.lastBook", (rc.villagerInfo != null && !rc.villagerInfo.bookless()) ? Str.getEnchantName(rc.villagerInfo.getEnchantment()) : Text.translatable("dev.paw.uniformity.cross")), x, y, Color.WHITE.asInt);
                    y += 11;
                    evt.ctx.drawTextWithShadow(mc.textRenderer, Text.stringifiedTranslatable("dev.paw.uniformity.displayInfo.recycler.lastValue", rc.villagerInfo != null && !rc.villagerInfo.bookless() ? rc.villagerInfo.getPrice() : Text.translatable("dev.paw.uniformity.cross")), x, y, Color.WHITE.asInt);
                }
            }
            evt.ctx.getMatrices().pop();
        }
    }

    private double getSpeed() {
        if (mc.player == null) {
            return lastSpeed;
        }
        if (lastPos == null) {
            lastPos = mc.player.getPos().multiply(1, 0, 1);
        }
        if (speedTimer.hasElapsed(50) && mc.player.getPos().distanceTo(lastPos) != 0) {
            lastSpeed = mc.player.getPos().multiply(1, 0, 1).distanceTo(lastPos) / 0.05D;
            lastPos = mc.player.getPos().multiply(1, 0, 1);
            speedTimer.reset();
        }
        return lastSpeed;
    }
}
