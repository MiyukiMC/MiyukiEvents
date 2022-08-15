package app.miyuki.miyukievents.bukkit.util.chat;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ChatUtils {


    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacy('&');

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @SneakyThrows
    public Component colorize(@NotNull String text) {
        Component legacyComponent = legacyComponentSerializer.deserialize(text);

        val message = miniMessage.serialize(legacyComponent)
                .replace("\\<", "<")
                .replace("\\\\<", "<");

        return miniMessage.deserialize(message);
    }

}