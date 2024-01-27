package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import org.lwjgl.glfw.GLFW;

public class Step extends KeyboundModule {
    public Step() {
        super("Step", GLFW.GLFW_KEY_J);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.stepToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.stepToggle = value;
    }

    @Override
    public void onClientTick() {
        if (mc.player == null) return;
        if (this.isEnabled()) {
            if (mc.player.isSneaking()) {
                mc.player.setStepHeight(0.6f);
            } else {
                mc.player.setStepHeight(1.25f);
            }
        } else if (mc.player.getStepHeight() != 0.6f) {
            mc.player.setStepHeight(0.6f);
        }
    }
}
