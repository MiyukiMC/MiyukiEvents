package app.miyuki.bukkit.messages;

import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.config.ConfigType;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Function;

@AllArgsConstructor
public class MessageDispatcher {

    private final ConfigProvider configProvider;

    public void dispatch(@NotNull CommandSender sender, @NotNull String path, @Nullable Function<String, String> format) {

        val messages = configProvider.provide(ConfigType.MESSAGES);

        if (!messages.contains(path)) {
            new ChatMessage(new String[]{ "&r[&9&lMiyuki&d&lEvents&r] &cMessage not found, contact an administrator." })
                    .dispatch(sender, format);
            return;
        }

        if (messages.isList(path)) {
            new ChatMessage(messages.getStringList(path).toArray(new String[0])).dispatch(sender, format);
            return;
        }

        val message = messages.getString(path);

        if (message.equalsIgnoreCase("json")) {

            val jsonFile = new File(messages.getFile().getParentFile(), "json/" + path);

            if (!jsonFile.exists()) {
                new ChatMessage(new String[]{ "&r[&9&lMiyuki&d&lEvents&r] &cJson file was not found." })
                        .dispatch(sender, format);
                return;
            }

            try {
                val json = new String(Files.readAllBytes(jsonFile.toPath()));
                new JsonMessage(json).dispatch(sender, format);
            } catch (IOException ex) {
                new ChatMessage(new String[]{ "&r[&9&lMiyuki&d&lEvents&r] &cCould not read json file." })
                        .dispatch(sender, format);
            }
            return;
        }

        new ChatMessage(new String[]{ message }).dispatch(sender, format);
    }

    public void dispatch(@NotNull CommandSender sender, @NotNull String path) {
        dispatch(sender, path, null);
    }



}