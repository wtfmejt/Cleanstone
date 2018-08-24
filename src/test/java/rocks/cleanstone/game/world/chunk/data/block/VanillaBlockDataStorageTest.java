package rocks.cleanstone.game.world.chunk.data.block;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rocks.cleanstone.game.block.Block;
import rocks.cleanstone.game.block.ImmutableBlock;
import rocks.cleanstone.game.block.state.BlockStateProvider;
import rocks.cleanstone.game.material.MaterialRegistry;
import rocks.cleanstone.game.material.SimpleMaterialRegistry;
import rocks.cleanstone.game.world.chunk.ArrayBlockDataTable;
import rocks.cleanstone.game.world.chunk.BlockDataTable;
import rocks.cleanstone.game.world.chunk.data.block.vanilla.DirectPalette;
import rocks.cleanstone.game.world.chunk.data.block.vanilla.VanillaBlockDataCodec;
import rocks.cleanstone.game.world.chunk.data.block.vanilla.VanillaBlockDataStorage;
import rocks.cleanstone.net.minecraft.protocol.v1_13.ProtocolBlockStateMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VanillaBlockDataStorageTest {

    private final Random random = new Random(1);
    private final DirectPalette directPalette = new DirectPalette(new ProtocolBlockStateMapping(), 14);
    private final MaterialRegistry materialRegistry = new SimpleMaterialRegistry();
    private VanillaBlockDataStorage storage;
    private final BlockStateProvider blockStateProvider = new BlockStateProvider();

    @BeforeEach
    void createStorageByTable() {
        blockStateProvider.init();
        BlockDataTable blockDataTable = new ArrayBlockDataTable(true);
        for (int i = 0; i < 20; i++) {
            Block randomBlock = ImmutableBlock.of(
                    new ArrayList<>(materialRegistry.getBlockTypes())
                            .get(random.nextInt(materialRegistry.getBlockTypes().size())));
            blockDataTable.setBlock(random.nextInt(16), random.nextInt(256), random.nextInt(16), randomBlock);
        }
        storage = new VanillaBlockDataStorage(blockDataTable, directPalette, true);
    }

    @Test
    void testSerializationAndTable() {
        VanillaBlockDataCodec codec = new VanillaBlockDataCodec(directPalette, true);
        ByteBuf serialized = codec.serialize(storage);
        VanillaBlockDataStorage deserialized;
        try {
            deserialized = codec.deserialize(serialized);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(storage.constructTable(), deserialized.constructTable());
        serialized.release();
    }

    @AfterEach
    void tearDown() {
        blockStateProvider.destroy();
    }
}
