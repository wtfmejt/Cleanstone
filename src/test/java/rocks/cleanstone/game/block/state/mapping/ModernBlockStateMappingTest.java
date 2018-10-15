package rocks.cleanstone.game.block.state.mapping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rocks.cleanstone.game.block.state.BlockState;
import rocks.cleanstone.game.material.MaterialRegistry;
import rocks.cleanstone.game.material.SimpleMaterialRegistry;
import rocks.cleanstone.net.minecraft.protocol.v1_13.ProtocolBlockStateMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModernBlockStateMappingTest {

    private BlockStateMapping<Integer> blockStateMapping;
    private MaterialRegistry materialRegistry;

    @BeforeEach
    void setUp() {
        blockStateMapping = new ProtocolBlockStateMapping();
        materialRegistry = new SimpleMaterialRegistry();
    }

    @Test
    void serializationShouldBeOneToOne() {
        materialRegistry.getBlockTypes().forEach(blockType -> {
            final BlockState state = BlockState.of(blockType);
            final BlockState deserialized = blockStateMapping.getState(blockStateMapping.getID(state));
            assertEquals(state, deserialized);
        });
    }
}