package rocks.cleanstone.net.protocol.minecraft.v1_12_2.handshake;

import io.netty.buffer.ByteBuf;
import rocks.cleanstone.net.packet.PacketFactory;
import rocks.cleanstone.net.packet.minecraft.receive.HandshakePacket;
import rocks.cleanstone.net.utils.ByteBufUtils;

import java.io.IOException;

public class HandshakeFactory implements PacketFactory<HandshakePacket> {

    @Override
    public HandshakePacket decode(ByteBuf byteBuf) throws IOException {
        final int version = ByteBufUtils.readVarInt(byteBuf);
        final String address = ByteBufUtils.readUTF8(byteBuf);
        final int port = byteBuf.readUnsignedShort();
        final int state = ByteBufUtils.readVarInt(byteBuf);

        return new HandshakePacket(version, address, port, state);
    }

    @Override
    public ByteBuf encode(ByteBuf byteBuf, HandshakePacket packet) throws IOException {
        ByteBufUtils.writeVarInt(byteBuf, packet.getVersion());
        ByteBufUtils.writeUTF8(byteBuf, packet.getAddress());
        byteBuf.writeShort(packet.getPort());
        ByteBufUtils.writeVarInt(byteBuf, packet.getState());

        return byteBuf;
    }
}
