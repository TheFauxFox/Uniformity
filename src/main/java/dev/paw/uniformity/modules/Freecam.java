package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.*;
import dev.paw.uniformity.utils.Color;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.dizitart.jbus.Subscribe;
import org.lwjgl.glfw.GLFW;

public class Freecam extends KeyboundModule {
    public boolean isInit = false;
    public float yaw, pitch, forwardSpeed, upSpeed, sideSpeed;
    public double x, y, z, prevX, prevY, prevZ, speed;
    public Vec3d velocity;

    public Freecam() {
        super("Freecam", GLFW.GLFW_KEY_U);
    }

    @Subscribe
    public void onCameraUpdate(CameraUpdateEvent evt) {
        if (mc.world == null || mc.player == null || mc.cameraEntity == null) return;
        if (isEnabled()) {
            if (!isInit) {
                isInit = true;

                mc.chunkCullingEnabled = false;

                if (mc.player.getVehicle() instanceof BoatEntity) {
                    ((BoatEntity) mc.player.getVehicle()).setInputs(false, false, false, false);
                }

                velocity = mc.player.getVelocity();

                pitch = evt.inverseView ? -mc.player.getPitch() : mc.player.getPitch();
                yaw = evt.inverseView ? mc.player.getYaw() + 180.0f : mc.player.getYaw();

                x = prevX = mc.gameRenderer.getCamera().getPos().getX();
                y = prevY = mc.gameRenderer.getCamera().getPos().getY();
                z = prevZ = mc.gameRenderer.getCamera().getPos().getZ();
            }

            evt.rotationCallback.setRotation(yaw, pitch);
            evt.positionCallback.setPosition(MathHelper.lerp(evt.tickDelta, prevX, x), MathHelper.lerp(evt.tickDelta, prevY, y), MathHelper.lerp(evt.tickDelta, prevZ, z));
            evt.cancel();
        } else if (isInit) {
            isInit = false;

            mc.chunkCullingEnabled = true;

            forwardSpeed = 0.0f;
            upSpeed = 0.0f;
            sideSpeed = 0.0f;
            speed = 0;
        }
    }

    @Subscribe
    public void onIsThirdPerson(CameraUpdateEvent.ThirdPerson evt) {
        if (isEnabled()) evt.setThirdPerson(true);
    }
    
    @Subscribe
    public void onDropItem(ClientPlayerEvent.OnDropItem evt) {
        if (isEnabled()) evt.cancel();
    }
    
    @Subscribe
    public void onHurt(ClientPlayerEvent.OnHealthChange evt) {
        if (isEnabled() && mc.player != null && mc.player.hurtTime == 10 && Uniformity.config.freecam.toggleOnDamage) setEnabled(false);
    }
    
    @Subscribe
    public void onTickMovement(ClientPlayerEvent.OnTickMovement evt) {
        if (!isEnabled() || mc.player == null || mc.world == null || velocity == null) return;
        mc.player.setVelocity(velocity);

        float forward = mc.player.input.movementForward;
        float up = (mc.player.input.jumping ? 1.0f : 0.0f) - (mc.player.input.sneaking ? 1.0f : 0.0f);
        float side = mc.player.input.movementSideways;

        forwardSpeed = forward != 0 ? evt.updateMotion(forwardSpeed, forward) : forwardSpeed * 0.5f;
        upSpeed = up != 0 ? evt.updateMotion(upSpeed, up) : upSpeed * 0.5f;
        sideSpeed = side != 0 ? evt.updateMotion(sideSpeed , side) : sideSpeed * 0.5f;

        double rotateX = Math.sin(yaw * Math.PI / 180.0D);
        double rotateZ = Math.cos(yaw * Math.PI / 180.0D);
        double speed = mc.player.isSprinting() ? 1.2D : 0.55D;

        speed += this.speed;
        speed = Math.max(0, speed);
        speed = Math.min(speed, 10);

        prevX = x;
        prevY = y;
        prevZ = z;

        x += (sideSpeed * rotateZ - forwardSpeed * rotateX) * speed;
        y += upSpeed * speed;
        z += (forwardSpeed * rotateZ + sideSpeed * rotateX) * speed;
    }

    @Subscribe
    public void onHasVehicle(ClientPlayerEvent.OnHasVehicle evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onJumpingMount(ClientPlayerEvent.OnGetJumpingMount evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onTickRiding(ClientPlayerEvent.OnTickRiding evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onMove(ClientPlayerEvent.OnMove evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onIsCamera(ClientPlayerEvent.OnIsCamera evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onIsSneaking(ClientPlayerEvent.OnIsSneaking evt) {
        if (isEnabled()) evt.setSneaking(false);
    }

    @Subscribe
    public void onChangeLookDirection(ClientPlayerEvent.OnChangeLookDirection evt) {
        if (isEnabled()) {
            yaw += (float) (evt.deltaX * 0.15D);
            pitch = (float) MathHelper.clamp(pitch + evt.deltaY * 0.15D, -90, 90);
            evt.cancel();
        }
    }

    @Subscribe
    public void onRenderHand(ClientPlayerEvent.OnRenderHand evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onAttack(ClientPlayerEvent.OnDoAttack.PRE evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onBlockBreak(ClientPlayerEvent.OnHandleBlockBreaking evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onItemUse(ClientPlayerEvent.ItemUseEvent.PRE evt) {
        if (isEnabled()) evt.cancel();
    }

    @Subscribe
    public void onHasOutline(EntityOutlineEvent evt) {
        if (isEnabled() && evt.entity == mc.player && Uniformity.config.freecam.highlightPlayer && !mc.options.hudHidden) evt.setOutline(true);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent evt) {
        setEnabled(false);
    }

    @Subscribe
    public void onMouseScroll(MouseScrollEvent evt) {
        Zoom zoom = Uniformity.getModule(Zoom.class);
        if (isEnabled() && zoom != null && !zoom.isZooming) {
            speed += evt.scrollDelta > 0.0 ? 0.05D : -0.05D;
            speed = Math.max(-0.5, speed);
            speed = Math.min(speed, 20);
            evt.cancel();
        }
    }

    @Subscribe
    public void onRenderLabel(EntityHasLabelEvent evt) {
        MobNameplates mn = Uniformity.getModule(MobNameplates.class);
        if (isEnabled() && evt.entity == mc.player && !mc.options.hudHidden && mn != null && !mn.isEnabled()) {
            evt.setHasLabel(true);
        }
    }

    @Subscribe
    public void setGlowColor(TeamColorEvent evt) {
        if (isEnabled() && evt.entity == mc.player && Uniformity.config.freecam.highlightPlayer) {
            evt.setColor(Color.hex(Uniformity.config.freecam.highlightHex));
        }
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
