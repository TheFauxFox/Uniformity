package dev.paw.uniformity.events;

import net.minecraft.entity.JumpingMount;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class ClientPlayerEvent {
    public static class OnRenderHand extends CancellableEvent {}
    public static class OnDropItem extends CancellableEvent {}
    public static class OnTickRiding extends CancellableEvent {}
    public static class OnMove extends CancellableEvent {}
    public static class OnIsCamera extends CancellableEvent {}
    public static class OnDamageTilt extends CancellableEvent {}

    public static class OnDoAttack {
        public static class PRE extends CancellableEvent {}
        public static class POST extends CancellableEvent {}
    }

    public static class ItemUseEvent {
        public static class PRE extends CancellableEvent {}
        public static class POST extends CancellableEvent {}
    }

    public static class OnHealthChange extends Event {
        public final float currentHealth;
        public final float newHealth;

        public OnHealthChange(float currentHealth, float newHealth) {
            this.currentHealth = currentHealth;
            this.newHealth = newHealth;
        }
    }

    public static class OnTickMovement extends CancellableEvent {
        public float updateMotion(float motion, float direction) {
            return (direction + motion == 0) ? 0.0f : MathHelper.clamp(motion + ((direction < 0) ? -0.35f : 0.35f), -1f, 1f);
        }
    }

    public static class OnSwingHand extends CancellableEvent {
        public final Hand hand;
        public OnSwingHand (Hand hand) {
            this.hand = hand;
        }
    }

    public static class OnHasVehicle extends CancellableEvent {
        public final boolean hasVehicle;
        public OnHasVehicle(boolean hasVehicle) {
            this.hasVehicle = hasVehicle;
        }
    }

    public static class OnGetJumpingMount extends CancellableEvent {
        public final JumpingMount mount;
        public OnGetJumpingMount(JumpingMount jumpingMount) {
            mount = jumpingMount;
        }
    }

    public static class OnIsSneaking extends Event {
        private boolean isSneaking;
        public OnIsSneaking(boolean sneaking) {
            isSneaking = sneaking;
        }

        public boolean isSneaking() {
            return isSneaking;
        }

        public void setSneaking(boolean sneaking) {
            isSneaking = sneaking;
        }
    }

    public static class OnChangeLookDirection extends CancellableEvent {
        public final double deltaX;
        public final double deltaY;
        public OnChangeLookDirection(double dX, double dY) {
            deltaX = dX;
            deltaY = dY;
        }
    }

    public static class OnHandleBlockBreaking extends CancellableEvent {
        public final boolean isBreaking;
        public OnHandleBlockBreaking(boolean breaking) {
            isBreaking = breaking;
        }
    }
}
