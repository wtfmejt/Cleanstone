package rocks.cleanstone.game.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import rocks.cleanstone.core.CleanstoneServer;
import rocks.cleanstone.game.Position;
import rocks.cleanstone.game.world.region.EntityManager;
import rocks.cleanstone.net.event.InboundPacketEvent;
import rocks.cleanstone.net.minecraft.packet.inbound.InPlayerPositionAndLookPacket;
import rocks.cleanstone.net.minecraft.packet.inbound.PlayerLookPacket;
import rocks.cleanstone.net.minecraft.packet.inbound.PlayerPositionPacket;
import rocks.cleanstone.player.PlayerManager;

public class EntityMovePacketListener {

    private final PlayerManager playerManager;
    private final EntityManager entityManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public EntityMovePacketListener(PlayerManager playerManager, EntityManager entityManager) {
        this.playerManager = playerManager;
        this.entityManager = entityManager;
    }

    @EventListener
    public void onPlayerLookPacket(InboundPacketEvent inboundPacketEvent) {
        if (!(inboundPacketEvent.getPacket() instanceof PlayerLookPacket)) {
            return;
        }

        PlayerLookPacket playerLookPacket = (PlayerLookPacket) inboundPacketEvent.getPacket();

        Entity entity = playerManager.getOnlinePlayer(inboundPacketEvent.getConnection()).getEntity();

        if (entity == null) {
            return;
        }

        Position oldPosition = entity.getPosition();
        Position newPosition = new Position(oldPosition);

        logger.info("Look event");

        //TODO: Set Direction in newPosition

        CleanstoneServer.publishEvent(new EntityMoveEvent(entity, oldPosition, newPosition));
    }

    @EventListener
    public void onPlayerPositionPacket(InboundPacketEvent inboundPacketEvent) {
        if (!(inboundPacketEvent.getPacket() instanceof PlayerPositionPacket)) {
            return;
        }

        PlayerPositionPacket playerPositionPacket = (PlayerPositionPacket) inboundPacketEvent.getPacket();

        Entity entity = playerManager.getOnlinePlayer(inboundPacketEvent.getConnection()).getEntity();

        if (entity == null) {
            return;
        }

        Position oldPosition = entity.getPosition();
        Position newPosition = new Position(oldPosition);

        newPosition.setX(playerPositionPacket.getX());
        newPosition.setY(playerPositionPacket.getFeetY());
        newPosition.setZ(playerPositionPacket.getZ());

        CleanstoneServer.publishEvent(new EntityMoveEvent(entity, oldPosition, newPosition));
    }

    @EventListener
    public void onPlayerPositionAndLookPacket(InboundPacketEvent inboundPacketEvent) {
        if (!(inboundPacketEvent.getPacket() instanceof InPlayerPositionAndLookPacket)) {
            return;
        }
        InPlayerPositionAndLookPacket playerPositionAndLookPacket = (InPlayerPositionAndLookPacket) inboundPacketEvent.getPacket();

        Entity entity = playerManager.getOnlinePlayer(inboundPacketEvent.getConnection()).getEntity();

        if (entity == null) {
            return;
        }

        Position oldPosition = entity.getPosition();
        Position newPosition = new Position(oldPosition);

        //TODO: Set Direction in newPosition

        newPosition.setX(playerPositionAndLookPacket.getX());
        newPosition.setY(playerPositionAndLookPacket.getY());
        newPosition.setZ(playerPositionAndLookPacket.getZ());

        CleanstoneServer.publishEvent(new EntityMoveEvent(entity, oldPosition, newPosition));
    }
}
