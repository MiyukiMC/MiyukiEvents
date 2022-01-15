package app.miyuki.bukkit.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public class ChatMessage implements Message {

    @Getter
    public String[] message;

    @Override
    public void dispatch(CommandSender sender) {
        sender.sendMessage(message);
    }

    @Override
    public void dispatch(CommandSender sender, Map<String, String> placeholders) {

        int i = 0;
        for (String message : this.message.clone()) {
            AtomicReference<String> tempMessage = new AtomicReference<>(message);
            placeholders.forEach((key, value) -> tempMessage.set(tempMessage.get().replace(key, value)));
            this.message[i] = tempMessage.get();
        }

        dispatch(sender);
    }

}
