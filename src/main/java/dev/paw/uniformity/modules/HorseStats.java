package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.RenderHorseScreenEvent;
import dev.paw.uniformity.mixins.accessors.IA_AbstractHorseEntity;
import dev.paw.uniformity.utils.Color;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import org.dizitart.jbus.Subscribe;

import java.text.DecimalFormat;

public class HorseStats extends Module {
    public HorseStats() {
        super("HorseStats");
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.horseStatsToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.horseStatsToggle = value;
    }

    @Subscribe
    public void drawHorseStats(RenderHorseScreenEvent evt) {
        if (mc.getWindow() == null || !isEnabled()) return;

        AbstractHorseEntity entity = evt.horse;

        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();
        TextRenderer textRenderer = mc.textRenderer;

        int x1 = (width / 2) - 173;
        int y1 = (height / 2) - 80;
        int x2 = (width / 2) - 88;
        int y2 = (height / 2) + 80;

        evt.ctx.fill(x1, y1, x2, y2, Color.rgba(5, 5, 5, 120).asInt);

        int textLeft = x1 + 4;
        int textTop = y1 + 5;

        DecimalFormat df = new DecimalFormat("#.##");

        double horseSpeed = entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 42.157796;
        double jumpHeight = 0;
        double jumpStrength = ((IA_AbstractHorseEntity)entity).getJumpStrength();

        while(jumpStrength > 0) {
            jumpHeight += jumpStrength;
            jumpStrength = (jumpStrength - .08) * .98 * .98;
        }

        if (entity instanceof LlamaEntity le) {
            evt.ctx.drawText(textRenderer, "Chest: " + le.getStrength() * 3, textLeft, textTop + 55, Color.WHITE.asInt, true);
            evt.ctx.drawText(textRenderer, "Chest: 3-15", textLeft, textTop + 132, Color.WHITE.asInt, true);
        }

        evt.ctx.drawText(textRenderer, "-=-  Stats  -=-", textLeft, textTop, Color.WHITE.asInt, true);

        evt.ctx.drawText(textRenderer, "Health: " + df.format(entity.getMaxHealth()), textLeft, textTop + 22, Color.WHITE.asInt, true);
        evt.ctx.drawText(textRenderer, "Speed: " + df.format(horseSpeed), textLeft, textTop + 33, Color.WHITE.asInt, true);
        evt.ctx.drawText(textRenderer, "Jump: " + df.format(jumpHeight), textLeft, textTop + 44, Color.WHITE.asInt, true);

        evt.ctx.drawText(textRenderer, "-=-   Max   -=-", textLeft, textTop + 77, Color.WHITE.asInt, true);

        evt.ctx.drawText(textRenderer, "Health: 15-30", textLeft, textTop + 99, Color.WHITE.asInt, true);
        evt.ctx.drawText(textRenderer, "Speed: 4.7-14.2", textLeft, textTop + 110, Color.WHITE.asInt, true);
        evt.ctx.drawText(textRenderer, "Jump: 1.1-5.2", textLeft, textTop + 121, Color.WHITE.asInt, true);
    }
}
