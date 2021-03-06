package rocks.cleanstone.net.minecraft.entity.metadata.entrydata;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import rocks.cleanstone.game.chat.message.Text;

@Slf4j
public class OptionalChatData implements EntityMetadataEntryData {
    @Nullable
    private final Text text;

    private OptionalChatData(@Nullable Text text) {
        this.text = text;
    }

    public static OptionalChatData of(@Nullable Text text) {
        return new OptionalChatData(text);
    }

    @Override
    public ByteBuf serialize() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBoolean(text != null);
        if (text != null) {
            ByteBuf chatData = ChatData.of(text).serialize();
            byteBuf.writeBytes(chatData);
            chatData.release();
        }
        return byteBuf;
    }
}
