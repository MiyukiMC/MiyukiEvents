package app.miyuki.miyukievents.bukkit.messages;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.Data;
import lombok.Getter;
import lombok.var;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Data(staticConstructor = "of")
public class ChatMessage implements Message {

    private final MiyukiEvents plugin;

    @Getter
    public final String message;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void dispatch(@NotNull CommandSender sender, @Nullable Function<String, String> format) {

        var tempMessage = message
                .replace("§", "")
                .replace("\\n", "<newline>");

        tempMessage = plugin.getTextColorAdapter().adapt(message);

        if (format != null) {
            tempMessage = format.apply(tempMessage);
        }

        plugin.getAdventure().sender(sender).sendMessage(miniMessage.deserialize(tempMessage));
    }

}
