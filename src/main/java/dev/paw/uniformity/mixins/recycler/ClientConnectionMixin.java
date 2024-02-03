package dev.paw.uniformity.mixins.recycler;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.ReCycler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        ReCycler rc = Uniformity.getModule(ReCycler.class);
        if (packet instanceof PlayerInteractEntityC2SPacket && rc != null) {
            if (rc.isEnabled() && MinecraftClient.getInstance().targetedEntity instanceof VillagerEntity ent && ent.getVillagerData().getProfession().equals(VillagerProfession.LIBRARIAN) && MinecraftClient.getInstance().options.sneakKey.isPressed()) {
                rc.villager = ent;
                rc.chatMsg("dev.paw.uniformity.recycler.setVillagerMessage");
            }
        }
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V"), cancellable = true)
    public void onReadPacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        ReCycler rc = Uniformity.getModule(ReCycler.class);
        if (rc == null) return;
        if ((packet instanceof CloseScreenS2CPacket || packet instanceof CloseHandledScreenC2SPacket) && rc.stepping) {
            ci.cancel();
        }
        if (packet instanceof OpenScreenS2CPacket p && (rc.stepping || MinecraftClient.getInstance().options.sneakKey.wasPressed())) {
            if (Objects.equals(p.getScreenHandlerType(), ScreenHandlerType.MERCHANT)) {
                ci.cancel();
            }
        }
        if (packet instanceof SetTradeOffersS2CPacket p && rc.isEnabled()) {
            rc.lastOffers = p.getOffers();
            Uniformity.logger.debug(p.getOffers().toNbt().toString());
        }
    }
}