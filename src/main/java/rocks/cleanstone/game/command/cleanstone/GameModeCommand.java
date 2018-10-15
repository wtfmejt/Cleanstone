package rocks.cleanstone.game.command.cleanstone;

import org.springframework.stereotype.Component;
import rocks.cleanstone.game.command.CommandMessage;
import rocks.cleanstone.game.command.SimpleCommand;
import rocks.cleanstone.game.gamemode.GameMode;
import rocks.cleanstone.game.gamemode.vanilla.VanillaGameMode;
import rocks.cleanstone.net.minecraft.packet.enums.GameStateChangeReason;
import rocks.cleanstone.net.minecraft.packet.outbound.ChangeGameStatePacket;
import rocks.cleanstone.player.Player;

import java.util.Collections;

@Component
public class GameModeCommand extends SimpleCommand {

    public GameModeCommand() {
        super("gamemode", Collections.singletonList("gm"), VanillaGameMode.class, Player.class);
    }

    @Override
    public void execute(CommandMessage message) {
        final GameMode gameMode = message.requireParameter(VanillaGameMode.class);
        final Player target = message.requireTargetPlayer();
        final boolean gameModeChanged = target.getGameMode() != gameMode;

        if (gameModeChanged) {
            target.setGameMode(gameMode);
            target.sendPacket(new ChangeGameStatePacket(GameStateChangeReason.CHANGE_GAMEMODE,
                    gameMode.getTypeId()));
            target.sendMessage("game.command.cleanstone.own-gamemode-changed", gameMode.getName());
        }
        if (target != message.getCommandSender() || !gameModeChanged) {
            message.getCommandSender().sendMessage("game.command.cleanstone.changed-gamemode",
                    target.getName(), gameMode.getName());
        }
    }
}
