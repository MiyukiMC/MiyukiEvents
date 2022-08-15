package app.miyuki.miyukievents.bukkit.messages;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import lombok.Data;
import lombok.Getter;
import lombok.var;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Data(staticConstructor = "of")
public class ChatMessage implements Message {

    private final MiyukiEvents plugin;

    @Getter
    public final String message;

    @Override
    public void dispatch(@NotNull CommandSender sender, @Nullable Function<String, String> format) {

        var tempMessage = message
                .replace("\\n", "<reset><newline>");

        if (format != null)
            tempMessage = format.apply(tempMessage);

        plugin.getAdventure().sender(sender).sendMessage(ChatUtils.colorize(tempMessage));
    }

}
