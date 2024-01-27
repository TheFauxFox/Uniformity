package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import org.lwjgl.glfw.GLFW;

public class Fullbright extends KeyboundModule {
    public Fullbright() {
        super("Fullbright", GLFW.GLFW_KEY_G);
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
