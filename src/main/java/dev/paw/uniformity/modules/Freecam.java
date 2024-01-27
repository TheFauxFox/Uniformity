package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Freecam extends KeyboundModule {
    public boolean isInit = false;
    public float yaw, pitch, forwardSpeed, upSpeed, sideSpeed;
    public double x, y, z, prevX, prevY, prevZ, speed;
    public Vec3d velocity;

    public Freecam() {
        super("Freecam", GLFW.GLFW_KEY_U);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.freecamToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.freecamToggle = value;
    }
}
