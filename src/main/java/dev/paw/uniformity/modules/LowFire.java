package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.RenderFireOverlayEvent;
import org.dizitart.jbus.Subscribe;

public class LowFire extends Module {
    public LowFire() {
        super("LowFire");
    }

    @Subscribe
    public void onRenderFire(RenderFireOverlayEvent evt) {
        if (isEnabled()) evt.matrixStack.translate(0, -0.3, 0);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.lowFireToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.lowFireToggle = value;
    }
}
