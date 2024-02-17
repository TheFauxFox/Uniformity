package dev.paw.uniformity.events;

import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

public class CameraUpdateEvent extends CancellableEvent {
    public final BlockView area;
    public final Entity focusedEntity;
    public final boolean thirdPerson;
    public final boolean inverseView;
    public final float tickDelta;
    public final SetRotationCallback rotationCallback;
    public final SetPositionCallback positionCallback;

    public CameraUpdateEvent(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, SetRotationCallback rotationCallback, SetPositionCallback positionCallback) {
        this.area = area;
        this.focusedEntity = focusedEntity;
        this.thirdPerson = thirdPerson;
        this.inverseView = inverseView;
        this.tickDelta = tickDelta;
        this.rotationCallback = rotationCallback;
        this.positionCallback = positionCallback;
    }

    public static class ThirdPerson extends Event {
        private boolean thirdPerson;

        public ThirdPerson(boolean isThirdPerson) {
            thirdPerson = isThirdPerson;
        }

        public boolean isThirdPerson() {
            return thirdPerson;
        }

        public void setThirdPerson(boolean thirdPerson) {
            this.thirdPerson = thirdPerson;
        }
    }

    public interface SetRotationCallback {
        void setRotation(float yaw, float pitch);
    }

    public interface SetPositionCallback {
        void setPosition(double x, double y, double z);
    }
}
