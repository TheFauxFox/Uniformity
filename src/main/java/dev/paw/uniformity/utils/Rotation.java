package dev.paw.uniformity.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Rotation {
    public static Rotations getNeededRotations(Vec3d vec) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return null;
        Vec3d eyesPos = MinecraftClient.getInstance().player.getEyePos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new Rotations(MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch));
    }

    public static class Rotations {
        public float yaw;
        public float pitch;

        public Rotations(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public void lookAt() {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.world == null || mc.player == null || mc.interactionManager == null) return;
            float y = MathHelper.wrapDegrees(yaw);
            float p = MathHelper.wrapDegrees(pitch);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(y, p, true));
        }

        public static Rotations from(LivingEntity entity) {
            return new Rotations(entity.getYaw(), entity.getPitch());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Rotation && ((Rotation) obj).yaw == yaw && ((Rotation) obj).pitch == pitch;
        }
    }
}
