package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import org.lwjgl.glfw.GLFW;

public class EntityOutline extends KeyboundModule {
    public EntityOutline() {
        super("EntityOutline", GLFW.GLFW_KEY_Y);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.entityOutlineToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.entityOutlineToggle = value;
    }
}
