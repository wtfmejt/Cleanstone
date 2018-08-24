package rocks.cleanstone.game.block.state.mapping;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rocks.cleanstone.game.block.state.BlockState;
import rocks.cleanstone.game.block.state.BlockStateProvider;
import rocks.cleanstone.game.material.MaterialRegistry;
import rocks.cleanstone.game.material.SimpleMaterialRegistry;
import rocks.cleanstone.net.minecraft.protocol.v1_13.ProtocolBlockStateMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModernBlockStateMappingTest {

    private final BlockStateMapping<Integer> blockStateMapping = new ProtocolBlockStateMapping();
    private final MaterialRegistry materialRegistry = new SimpleMaterialRegistry();
    private final BlockStateProvider blockStateProvider = new BlockStateProvider();

    @BeforeEach
    void setUp() {
        blockStateProvider.init();
    }

    @Test
    void serializationShouldBeOneToOne() {
        materialRegistry.getBlockTypes().forEach(blockType -> {
            BlockState state = BlockState.of(blockType);
            BlockState deserialized = blockStateMapping.getState(blockStateMapping.getID(state));
            assertEquals(state, deserialized);
        });
    }

    @AfterEach
    void tearDown() {
        blockStateProvider.destroy();
    }
}