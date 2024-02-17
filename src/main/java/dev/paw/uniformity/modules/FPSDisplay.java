package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.Render2dEvent;
import dev.paw.uniformity.utils.Color;
import org.dizitart.jbus.Subscribe;

public class FPSDisplay extends KeyboundModule {
    public FPSDisplay() {
        super("FpsDisplay", -1);
    }

    @Subscribe
    public void onRender(Render2dEvent evt) {
        if (evt.hideHud()) {
            return;
        }

        if (isEnabled()) {
            evt.ctx.getMatrices().push();
            evt.ctx.drawTextWithShadow(mc.textRenderer, mc.getCurrentFps() + " FPS", 1, 1, Color.WHITE.asInt);
            evt.ctx.getMatrices().pop();
        }
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.fpsToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.fpsToggle = value;
    }
}
