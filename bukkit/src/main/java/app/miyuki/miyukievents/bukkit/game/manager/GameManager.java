package app.miyuki.miyukievents.bukkit.game.manager;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.*;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public class GameManager {

    public final @NonNull MiyukiEvents plugin;
    private final @NonNull Config config;
    private final @NonNull String language;

    private static final ImmutableList<String> DEFAULT_GAMES = ImmutableList.of(
            "word",
            "math",
            "lottery",
            "pool"
    );

    @Getter
    private final Map<String, Game> games = Maps.newHashMap();

    @Nullable
    public Game getCurrentGame() {
        for (Game game : plugin.getGameManager().getGames().values()) {

            if (game.getGameState() != GameState.QUEUE && game.getGameState() != GameState.STOPPED) {
                return game;
            }

        }
        return null;
    }

    public void load() {

        List<String> games = Lists.newArrayList();

        games.addAll(DEFAULT_GAMES);

        games.addAll(config.getStringList("CustomEvents"));

        for (String gameName : games) {

            val path = "events/" + gameName + "/";
            String internalPath = null;

            if (games.contains(gameName)) {
                internalPath = language + "/" + path;
            }

            val configProvider = new GameConfigProvider(path, internalPath);

            val config = configProvider.provide(ConfigType.CONFIG);

            val typeName = config.getString("Type");

            val type = Arrays.stream(GameType.values())
                    .filter(it -> it.name().equalsIgnoreCase(typeName))
                    .findFirst();

            val console = Bukkit.getConsoleSender();

            if (!type.isPresent()) {
                console.sendMessage(ChatUtils.colorize("&r[&9&lMiyuki&d&lEvents&r] &cThe type &f" + typeName + " is invalid."));
                continue;
            }

            val game = type.get().instantiate(configProvider);

            if (game == null) {
                continue;
            }

            plugin.getCommandRegistry().register(game, type.get().getCommandClass());

            this.games.put(gameName.toLowerCase(Locale.ROOT), game);
        }
    }

    public void reload() {

    }

    public void unload() {

    }
}