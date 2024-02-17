package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.GetBrightnessEvent;
import org.dizitart.jbus.Subscribe;
import org.lwjgl.glfw.GLFW;

public class Fullbright extends KeyboundModule {
    public Fullbright() {
        super("Fullbright", GLFW.GLFW_KEY_G);
    }

    @Subscribe
    public void onGetBrightness(GetBrightnessEvent evt) {
        if (isEnabled()) evt.setBrightness(1);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.fullbrightToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.fullbrightToggle = value;
    }
}
