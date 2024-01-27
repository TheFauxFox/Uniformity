package dev.paw.uniformity.mixins.freecam;

import com.mojang.authlib.GameProfile;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.Freecam;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.JumpingMount;
import net.minecraft.util.math.MathHelper;

@Mixin(ClientPlayerEntity.class)
abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	@Shadow	public abstract JumpingMount getJumpingMount();

	@Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
	private void onDropSelectedItem(CallbackInfoReturnable<Boolean> info) {
		if(Uniformity.config.freecamToggle) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "updateHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getHealth()F", ordinal = 0))
	private void onUpdateHealth(float health, CallbackInfo info) {
		if(this.hurtTime == 10 && Uniformity.config.freecamToggle && Uniformity.config.freecam.toggleOnDamage) {
			Uniformity.config.freecamToggle = false;
		}
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void onTickMovement(CallbackInfo ci) {
		Freecam fc = Uniformity.getModule(Freecam.class);
		MinecraftClient mc = MinecraftClient.getInstance();
		if (fc == null || mc.player == null || mc.world == null) return;
		if(Uniformity.config.freecamToggle) {
			this.setVelocity(fc.velocity);

			float forward = mc.player.input.movementForward;
			float up = (mc.player.input.jumping ? 1.0f : 0.0f) - (mc.player.input.sneaking ? 1.0f : 0.0f);
			float side = mc.player.input.movementSideways;

			fc.forwardSpeed = forward != 0 ? updateMotion(fc.forwardSpeed, forward) : fc.forwardSpeed * 0.5f;
			fc.upSpeed = up != 0 ?  updateMotion(fc.upSpeed, up) : fc.upSpeed * 0.5f;
			fc.sideSpeed = side != 0 ?  updateMotion(fc.sideSpeed , side) : fc.sideSpeed * 0.5f;

			double rotateX = Math.sin(fc.yaw * Math.PI / 180.0D);
			double rotateZ = Math.cos(fc.yaw * Math.PI / 180.0D);
			double speed = mc.player.isSprinting() ? 1.2D : 0.55D;

			speed += fc.speed;
			speed = Math.max(0, speed);
			speed = Math.min(speed, 10);

			fc.prevX = fc.x;
			fc.prevY = fc.y;
			fc.prevZ = fc.z;

			fc.x += (fc.sideSpeed * rotateZ - fc.forwardSpeed * rotateX) * speed;
			fc.y += fc.upSpeed * speed;
			fc.z += (fc.forwardSpeed * rotateZ + fc.sideSpeed * rotateX) * speed;
		}
	}

	@Unique
	private float updateMotion(float motion, float direction) {
		return (direction + motion == 0) ? 0.0f : MathHelper.clamp(motion + ((direction < 0) ? -0.35f : 0.35f), -1f, 1f);
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasVehicle()Z", ordinal = 0))
	private boolean hijackHasVehicle(ClientPlayerEntity player) {
		if(Uniformity.config.freecamToggle) {
			return false;
		}

		return this.hasVehicle();
	}

	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getJumpingMount()Lnet/minecraft/entity/JumpingMount;", ordinal = 0))
	private JumpingMount hijackGetJumpingMount(ClientPlayerEntity player) {
		if(Uniformity.config.freecamToggle) {
			return null;
		}

		return this.getJumpingMount();
	}

	@Inject(method = "tickRiding", at = @At("HEAD"), cancellable = true)
	private void onTickRiding(CallbackInfo info) {
		if(Uniformity.config.freecamToggle) {
			super.tickRiding();
			info.cancel();
		}
	}

	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void onMove(CallbackInfo info) {
		if(Uniformity.config.freecamToggle) {
			info.cancel();
		}
	}

	@Inject(method = "isCamera", at = @At("HEAD"), cancellable = true)
	private void onIsCamera(CallbackInfoReturnable<Boolean> info) {
		if(Uniformity.config.freecamToggle) {
			info.setReturnValue(false);
		}
	}

	@Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
	private void onIsSneaking(CallbackInfoReturnable<Boolean> info)	{
		if(Uniformity.config.freecamToggle) {
			info.setReturnValue(false);
		}
	}

	@Override
	public void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
		Freecam fc = Uniformity.getModule(Freecam.class);
		if (fc == null) return;
		if(Uniformity.config.freecamToggle) {
			fc.yaw += (float) (cursorDeltaX * 0.15D);
			fc.pitch = (float) MathHelper.clamp(fc.pitch + cursorDeltaY * 0.15D, -90, 90);
		}
		else {
			super.changeLookDirection(cursorDeltaX, cursorDeltaY);
		}
	}

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
}
