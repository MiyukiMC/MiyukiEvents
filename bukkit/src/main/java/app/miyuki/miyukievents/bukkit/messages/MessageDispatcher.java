package app.miyuki.miyukievents.bukkit.messages;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.Config;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class MessageDispatcher {

    private final LoadingCache<String, Message> cache;

    public MessageDispatcher(MiyukiEvents plugin, @NotNull Config messages) {

        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Message>() {
                    @SneakyThrows
                    @Override
                    public Message load(final @NotNull String path) {

                        val messagesRoot = messages.getRoot();

                        if (!messagesRoot.hasChild(path)) {
                            return ChatMessage.of(
                                    plugin,
                                    "<gray>[<gradient:#2A8CFF:#25FFE5>MiyukiEvents</gradient><gray>] <red>Message '<gray>" + path + "' <red>not found, contact an administrator."
                            );
                        }

                        val messageNode = messagesRoot.node(path);

                        if (messageNode.isList()) {
                            return ChatMessage.of(
                                    plugin,
                                    String.join("<reset><newline>", messageNode.getList(String.class, ArrayList::new))
                            );
                        }

                        return ChatMessage.of(plugin, messageNode.getString());
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

    public void clear() {
        cache.invalidateAll();
    }

}
