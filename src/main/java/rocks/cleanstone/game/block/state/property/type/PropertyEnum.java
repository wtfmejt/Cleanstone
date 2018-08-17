package rocks.cleanstone.game.block.state.property.type;

import com.google.common.math.IntMath;

import java.math.RoundingMode;

public class PropertyEnum<E extends Enum<E>> extends AbstractProperty<E> {

    private final int maxSerializationBits;
    private final Class<E> enumClass;

    public PropertyEnum(String key, E defaultValue) {
        super(key, defaultValue);
        enumClass = defaultValue.getDeclaringClass();
        this.maxSerializationBits = IntMath.log2(enumClass.getEnumConstants().length, RoundingMode.CEILING);
    }

    @Override
    public int serialize(E value) {
        return value.ordinal();
    }

    @Override
    public E deserialize(int serializedValue) {
        return enumClass.getEnumConstants()[serializedValue];
    }

    @Override
    public int getNeededSerializationBitAmount() {
        return maxSerializationBits;
    }

    @Override
    public Class<E> getValueClass() {
        return enumClass;
    }
}