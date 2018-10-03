package rocks.cleanstone.game.world.generation;

import java.util.Map;

import rocks.cleanstone.game.entity.RotatablePosition;
import rocks.cleanstone.game.world.chunk.Chunk;
import rocks.cleanstone.game.world.chunk.ChunkCoords;

public interface WorldGenerator {

    Chunk generateChunk(int seed, ChunkCoords coords);

    RotatablePosition getFirstSpawnPosition(int seed);

    void setGenerationParameter(WorldGenerationParameter parameter, double value);

    double getGenerationParameter(WorldGenerationParameter parameter);

    Map<WorldGenerationParameter, Double> getGenerationParameters();

    String getName();
}
