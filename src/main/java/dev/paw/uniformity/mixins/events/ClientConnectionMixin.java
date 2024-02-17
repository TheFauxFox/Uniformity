package dev.paw.uniformity.mixins.events;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        PacketEvent.OnSend onSend = Uniformity.bus.post(new PacketEvent.OnSend(packet));
        if (onSend.isCancelled()) ci.cancel();
        packet = onSend.getPacket();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V"), cancellable = true)
    public void onReadPacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        PacketEvent.OnRecieved onRecieved = Uniformity.bus.post(new PacketEvent.OnRecieved(packet));
        if (onRecieved.isCancelled()) ci.cancel();
        packet = onRecieved.getPacket();
    }
}