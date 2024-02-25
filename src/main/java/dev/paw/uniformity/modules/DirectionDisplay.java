package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.Render2dEvent;
import dev.paw.uniformity.utils.Color;
import dev.paw.uniformity.utils.Number;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.dizitart.jbus.Subscribe;

public class DirectionDisplay extends KeyboundModule {
    public DirectionDisplay() {
        super("DirectionDisplay", -1);
    }

    @Subscribe
    public void onRender2d(Render2dEvent evt) {
        if (!isEnabled() || mc.player == null) return;
        int w = mc.getWindow().getScaledWidth();

        int widPercent = (int) (w * 30 * 0.01);
        int height = 12;
        int padTop = 2;
        Number.Centered width = Number.Centered.fromInt(widPercent);
        Number.Centered reticle = Number.Centered.fromInt(2);

        Color backgroundColor = Color.hex(Uniformity.config.directionDisplay.backgroundHex).withAlpha(Uniformity.config.directionDisplay.backgroundAlpha);
        Color textColor = Color.hex(Uniformity.config.directionDisplay.textColorHex).withAlpha(Uniformity.config.directionDisplay.textColorAlpha);
        Color reticleColor = Color.hex(Uniformity.config.directionDisplay.reticleHex).withAlpha(Uniformity.config.directionDisplay.reticleAlpha);

        float facingPerc = MathHelper.wrapDegrees(MathHelper.lerp(evt.tickDelta, mc.player.lastRenderYaw, mc.player.renderYaw) + 180) / 360;
        float facing = 256 * facingPerc;

        evt.ctx.fill(width.x1(), padTop, width.x2(), padTop + height, backgroundColor.asInt);
        evt.ctx.fill(reticle.x1(), 10, reticle.x1() + 1, height + padTop, reticleColor.asInt);
        textColor.setShader();
        evt.ctx.drawTexture(new Identifier("uniformity:compass.png"), width.x1(), padTop + (padTop / 2), 132 + facing - ((float) w / widPercent) - width.getWidth() / 2F, 0, width.getWidth(), 12, 256, 12);
        Color.resetShader();
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.directionDisplayToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.directionDisplayToggle = value;
    }
}
