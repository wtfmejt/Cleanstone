package rocks.cleanstone.net.mcpe.packet.outbound;

import rocks.cleanstone.net.mcpe.packet.MCPEOutboundPacketType;
import rocks.cleanstone.net.packet.Packet;
import rocks.cleanstone.net.packet.PacketType;

public class AvailableCommandsPacket implements Packet {


    public AvailableCommandsPacket() {

    }


    @Override
    public PacketType getType() {
        return MCPEOutboundPacketType.AVAILABLE_COMMANDS;
    }
}

