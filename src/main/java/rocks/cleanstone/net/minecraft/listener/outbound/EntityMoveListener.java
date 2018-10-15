package rocks.cleanstone.net.minecraft.listener.outbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import rocks.cleanstone.game.entity.Entity;
import rocks.cleanstone.game.entity.EntityTracker;
import rocks.cleanstone.game.entity.HeadRotatablePosition;
import rocks.cleanstone.game.entity.event.EntityMoveEvent;
import rocks.cleanstone.net.minecraft.packet.outbound.*;
import rocks.cleanstone.player.Player;

import java.util.Collection;

@Component
public class EntityMoveListener {

    private final EntityTracker entityTracker;

    @Autowired
    public EntityMoveListener(EntityTracker entityTracker) {
        this.entityTracker = entityTracker;
    }

    @Async
    @EventListener
    public void onEntityMove(EntityMoveEvent event) {
        final HeadRotatablePosition oldPosition = event.getOldPosition();
        final HeadRotatablePosition newPosition = event.getNewPosition();
        final Entity movingEntity = event.getEntity();
        // TODO check if the observers use the current protocol layer
        final Collection<Player> observers = entityTracker.getPlayerObservers(movingEntity);
        final int entityID = movingEntity.getEntityID();
        final boolean isFlying = movingEntity instanceof Player && ((Player) movingEntity).isFlying();
        final float pitch = newPosition.getRotation().getPitch();
        final float yaw = newPosition.getRotation().getYaw();

        if (!oldPosition.getHeadRotation().equals(newPosition.getHeadRotation())) {
            final EntityHeadLookPacket entityHeadLookPacket = new EntityHeadLookPacket(
                    entityID, newPosition.getHeadRotation().getYaw());
            observers.forEach(observer -> observer.sendPacket(entityHeadLookPacket));
        }

        if (oldPosition.equalCoordinates(newPosition)) {
            if (oldPosition.getRotation().equals(newPosition.getRotation())) {
                return;
            }

            final EntityLookPacket entityLookPacket = new EntityLookPacket(entityID, yaw, pitch, isFlying);
            observers.forEach(observer -> observer.sendPacket(entityLookPacket));
            return;
        }

        final double deltaX = (newPosition.getX() * 32 - oldPosition.getX() * 32) * 128;
        final double deltaY = (newPosition.getY() * 32 - oldPosition.getY() * 32) * 128;
        final double deltaZ = (newPosition.getZ() * 32 - oldPosition.getZ() * 32) * 128;

        if (isTeleport(deltaX, deltaY, deltaZ)) {
            final EntityTeleportPacket entityTeleportPacket = new EntityTeleportPacket(entityID, newPosition.getX(),
                    newPosition.getY(), newPosition.getZ(), yaw, pitch, isFlying);
            observers.forEach(observer -> observer.sendPacket(entityTeleportPacket));
            return;
        }

        if (oldPosition.getRotation().equals(newPosition.getRotation())) {
            final EntityRelativeMovePacket entityRelativeMovePacket = new EntityRelativeMovePacket(entityID,
                    ((short) deltaX), ((short) deltaY), ((short) deltaZ), isFlying);

            observers.forEach(observer -> observer.sendPacket(entityRelativeMovePacket));
            return;
        }

        final EntityLookAndRelativeMovePacket entityLookAndRelativeMovePacket = new EntityLookAndRelativeMovePacket(
                entityID, ((short) deltaX), ((short) deltaY), ((short) deltaZ), yaw, pitch, isFlying);

        observers.forEach(observer -> observer.sendPacket(entityLookAndRelativeMovePacket));
    }

    private boolean isTeleport(double deltaX, double deltaY, double deltaZ) {
        return deltaX > Short.MAX_VALUE || deltaY > Short.MAX_VALUE || deltaZ > Short.MAX_VALUE
                || deltaX < Short.MIN_VALUE || deltaY < Short.MIN_VALUE || deltaZ < Short.MIN_VALUE;
    }
}
