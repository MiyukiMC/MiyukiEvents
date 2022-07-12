package app.miyuki.miyukievents.bukkit.messages;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class MessageDispatcher {

    private final LoadingCache<String, Message> cache;

    public MessageDispatcher(MiyukiEvents plugin, @NotNull GameConfigProvider configProvider) {
        this(plugin, configProvider.provide(ConfigType.MESSAGES));
    }

    public MessageDispatcher(MiyukiEvents plugin, @NotNull Config messages) {

        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Message>() {
                    @Override
                    public Message load(final @NotNull String path) {

                        if (!messages.contains(path)) {
                            return ChatMessage.of(
                                    plugin,
                                    "&r[&9&lMiyuki&d&lEvents&r] &cMessage '&7" + path + "' &cnot found, contact an administrator."
                            );
                        }


                        if (messages.isList(path)) {
                            return ChatMessage.of(plugin, String.join("<newline>", messages.getStringList(path)));
                        }

                        return ChatMessage.of(plugin, messages.getString(path));
                    }

                });
    }

    @SneakyThrows
    public void dispatch(@NotNull CommandSender sender, @NotNull String path, @Nullable Function<String, String> format) {
        cache.get(path).dispatch(sender, format);
    }

    public void dispatch(@NotNull CommandSender sender, @NotNull String path) {
        dispatch(sender, path, null);
    }

    public void globalDispatch(@NotNull String path) {
        Bukkit.getOnlinePlayers().forEach(player -> dispatch(player, path, null));
    }

    public void globalDispatch(@NotNull String path, @Nullable Function<String, String> format) {
        Bukkit.getOnlinePlayers().forEach(player -> dispatch(player, path, format));
    }


}
