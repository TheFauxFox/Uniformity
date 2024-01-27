package dev.paw.uniformity.mixins.freecam;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.Freecam;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;

@Mixin(Camera.class)
abstract class CameraMixin {
	@Shadow
	private boolean ready;

	@Shadow
	private BlockView area;

	@Shadow
	private Entity focusedEntity;

	@Shadow
	private boolean thirdPerson;

	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Shadow
	protected abstract void setPos(double x, double y, double z);

	@Inject(method = "update", at = @At("HEAD"), cancellable = true)
	private void onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
		Freecam fc = Uniformity.getModule(Freecam.class);
		MinecraftClient mc = MinecraftClient.getInstance();
		if (fc == null || mc.world == null || mc.player == null) return;
		if (Uniformity.config.freecamToggle) {
			if (!fc.isInit) {
				fc.isInit = true;

				mc.chunkCullingEnabled = false;

				if (mc.player.getVehicle() instanceof BoatEntity) {
					((BoatEntity) mc.player.getVehicle()).setInputs(false, false, false, false);
				}

				fc.velocity = mc.player.getVelocity();

				fc.pitch = inverseView ? -mc.player.getPitch() : mc.player.getPitch();
				fc.yaw = inverseView ? mc.player.getYaw() + 180.0f : mc.player.getYaw();

				fc.x = fc.prevX = mc.gameRenderer.getCamera().getPos().getX();
				fc.y = fc.prevY = mc.gameRenderer.getCamera().getPos().getY();
				fc.z = fc.prevZ = mc.gameRenderer.getCamera().getPos().getZ();
			}

			this.ready = true;
			this.area = area;
			this.focusedEntity = focusedEntity;
			this.thirdPerson = thirdPerson;

			this.setRotation(fc.yaw, fc.pitch);
			this.setPos(MathHelper.lerp(tickDelta, fc.prevX, fc.x), MathHelper.lerp(tickDelta, fc.prevY, fc.y), MathHelper.lerp(tickDelta, fc.prevZ, fc.z));

			info.cancel();
		} else if (fc.isInit) {
			fc.isInit = false;

			mc.chunkCullingEnabled = true;

			fc.forwardSpeed = 0.0f;
			fc.upSpeed = 0.0f;
			fc.sideSpeed = 0.0f;
			fc.speed = 0;
		}
	}

	@Inject(method = "isThirdPerson", at = @At("HEAD"), cancellable = true)
	private void onIsThirdPerson(CallbackInfoReturnable<Boolean> info) {
		if (Uniformity.config.freecamToggle) {
			info.setReturnValue(true);
		}
	}
}
