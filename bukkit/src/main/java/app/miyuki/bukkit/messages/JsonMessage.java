package app.miyuki.bukkit.messages;

import app.miyuki.bukkit.util.gson.GsonProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public class JsonMessage implements Message {

    @Getter
    public String message;

    @Override
    public void dispatch(CommandSender sender) {
        val textComponent = new TextComponent("");
        for (BaseComponent baseComponent : GsonProvider.getPrettyPrinting().fromJson(message, BaseComponent[].class)) {
            textComponent.addExtra(baseComponent);
        }
        ((Player) sender).spigot().sendMessage(textComponent);
    }

    @Override
    public void dispatch(CommandSender sender, Map<String, String> placeholders) {
        AtomicReference<String> tempMessage = new AtomicReference<>(message);

        placeholders.forEach((key, value) -> tempMessage.set(tempMessage.get().replace(key, value)));

        this.message = tempMessage.get();
        dispatch(sender);
    }

}
