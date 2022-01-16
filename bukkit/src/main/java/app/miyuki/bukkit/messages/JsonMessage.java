package app.miyuki.bukkit.messages;

import app.miyuki.bukkit.util.chat.ChatUtils;
import app.miyuki.bukkit.util.json.GsonProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.val;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
@Data
public class JsonMessage implements Message {

    @Getter
    public String message;

    @Override
    public void dispatch(@NotNull CommandSender sender, @Nullable Function<String, String> format) {

        message = ChatUtils.colorize(message.replace("\\n", "\n"));

        if (format != null) {
            this.message = format.apply(this.message);
        }

        val textComponent = new TextComponent("");
        for (BaseComponent baseComponent : GsonProvider.getPrettyPrinting().fromJson(message, BaseComponent[].class)) {
            textComponent.addExtra(baseComponent);
        }
        ((Player) sender).spigot().sendMessage(textComponent);
    }


}
