package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientTickEvent;
import dev.paw.uniformity.utils.Rotation;
import net.minecraft.block.BlockState;
import net.minecraft.block.CaveVinesHeadBlock;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;

public class SnipperAura extends KeyboundModule {
    public SnipperAura() {
        super("SniperAura", -1);
    }

    @Override
    public void onClientTick(ClientTickEvent evt) {
        if (!isEnabled() || mc.player == null || mc.world == null || mc.interactionManager == null || !mc.player.isHolding(Items.SHEARS)) return;
        for (int x = -4; x <= 4; x ++) {
            for (int z = -4; z <= 4; z ++) {
                for (int y = -4; y <= 4; y ++) {
                    BlockPos pos = mc.player.getBlockPos().add(x, y, z);
                    BlockState state = mc.world.getBlockState(pos);
                    if (state.getBlock() instanceof CaveVinesHeadBlock vine && !vine.hasMaxAge(state)) {
                        Vec3d posVec = Vec3d.ofCenter(pos);
                        Vec3d eyesPos = mc.player.getEyePos();
                        double distanceSqPosVec = eyesPos.squaredDistanceTo(posVec);
                        for(Direction side : Direction.values()) {
                            BlockPos neighbor = pos.offset(side);
                            if(mc.world.getBlockState(neighbor).getOutlineShape(mc.world, pos) == VoxelShapes.empty() || mc.world.getBlockState(neighbor).isReplaceable()) continue;
                            Vec3d dirVec = Vec3d.of(side.getVector());
                            Vec3d hitVec = posVec.add(dirVec.multiply(0.5));
                            if (eyesPos.squaredDistanceTo(hitVec) > mc.interactionManager.getReachDistance()) continue;
                            if (distanceSqPosVec > eyesPos.squaredDistanceTo(posVec.add(dirVec))) continue;
                            if (mc.world.raycast(new RaycastContext(eyesPos, hitVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getType() != HitResult.Type.MISS) continue;
                            Rotation.Rotations rotation = Rotation.getNeededRotations(hitVec);
                            if (rotation == null) return;
                            rotation.lookAt();
                            BlockHitResult hitResult = new BlockHitResult(hitVec, side, pos, false);
                            Hand hand = mc.player.getMainHandStack().getItem().equals(Items.SHEARS) ? Hand.MAIN_HAND : Hand.OFF_HAND;
                            mc.interactionManager.interactBlock(mc.player, hand, hitResult);
                            mc.interactionManager.interactItem(mc.player, hand);
                            mc.player.swingHand(hand);
                            Uniformity.sendPrefixedMessage(Text.of("Snipped"));
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.snipperAuraToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.snipperAuraToggle = value;
    }
}
