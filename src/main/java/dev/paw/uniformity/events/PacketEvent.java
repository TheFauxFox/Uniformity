package dev.paw.uniformity.events;

import net.minecraft.network.packet.Packet;

public class PacketEvent {
    public static class OnSend extends CancellableEvent {
        private Packet<?> packet;

        public OnSend(Packet<?> packet) {
            this.packet = packet;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        @SuppressWarnings("unused")
        public void setPacket(Packet<?> packet) {
            this.packet = packet;
        }
    }

    public static class OnRecieved extends CancellableEvent {
        private Packet<?> packet;

        public OnRecieved(Packet<?> packet) {
            this.packet = packet;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        @SuppressWarnings("unused")
        public void setPacket(Packet<?> packet) {
            this.packet = packet;
        }
    }
}
