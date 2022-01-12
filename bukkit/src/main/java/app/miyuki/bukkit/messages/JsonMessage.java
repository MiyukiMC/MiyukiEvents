package app.miyuki.bukkit.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.Map;

@AllArgsConstructor
public class JsonMessage implements Message {

    @Getter
    public String message;

    @Override
    public void dispatch(CommandSender sender) {

    }

    @Override
    public void dispatch(CommandSender sender, Map<String, String> placeholders) {

    }

}
