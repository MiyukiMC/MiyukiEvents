package app.miyuki.bukkit.game.manager;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.config.Config;
import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameType;
import app.miyuki.bukkit.util.chat.ChatUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public class GameManager implements Manager {

    private final @NonNull Config config;
    private final @NonNull String language;

    private static final ImmutableList<String> DEFAULT_GAMES = ImmutableList.of(
            "anvil",
            "archer",
            "fastclick",
            "fight",
            "giveaway",
            "gladiador",
            "killer",
            "lottery",
            "math",
            "spleef",
            "sumo",
            "word"
    );

    @Getter
    private final Map<String, Game> games = Maps.newHashMap();

    public final @NonNull MiyukiEvents plugin;

    @Override
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

            val configProvider = new ConfigProvider(path, internalPath);

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

            this.games.put(gameName.toLowerCase(Locale.ROOT), game);
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public void unload() {

    }
}
