package app.miyuki.bukkit.messages;

import app.miyuki.bukkit.util.chat.ChatUtils;
import lombok.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@AllArgsConstructor
@Data
public class ChatMessage implements Message {

    @Getter
    public String[] message;

    @Override
    public void dispatch(@NotNull CommandSender sender, @Nullable Function<String, String> format) {

        int i = 0;
        for (String message : this.message.clone()) {

            var tempMessage = ChatUtils.colorize(message.replace("\\n", "\n"));

            if (format != null) {
                tempMessage = format.apply(tempMessage);
            }

            this.message[i] = tempMessage;
        }

        sender.sendMessage(message);
    }

}
