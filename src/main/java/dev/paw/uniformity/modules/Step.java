package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientTickEvent;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
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

    public void setStepHeight(float height) {
        if (mc.player == null) return;
        EntityAttributeInstance attribs = mc.player.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT);
        if (attribs == null) return;
        attribs.setBaseValue(height);
    }

    @Override
    public void onClientTick(ClientTickEvent evt) {
        if (mc.player == null) return;
        if (this.isEnabled()) {
            if (mc.player.isSneaking()) {
                this.setStepHeight(0.6f);
            } else {
                this.setStepHeight(1.25f);
            }
        } else if (mc.player.getStepHeight() != 0.6f) {
            this.setStepHeight(0.6f);
        }
    }
}
