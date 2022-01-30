package app.miyuki.miyukievents.bukkit.messages;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface Message {

    void dispatch(@NotNull CommandSender sender, @Nullable Function<String, String> format);

}
