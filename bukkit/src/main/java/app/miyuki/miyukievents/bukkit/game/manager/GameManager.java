package app.miyuki.miyukievents.bukkit.game.manager;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.*;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GameManager {

    public final @NonNull MiyukiEvents plugin;
    private final @NonNull String language;

    private static final ImmutableList<String> DEFAULT_GAMES = ImmutableList.of(
            "word",
            "math",
            "lottery",
            "pool",
            "fastquiz",
            "jackpot",
            "fastclick"
    );

    @Getter
    private final Map<String, Game<?>> games = Maps.newHashMap();

    @Nullable
    public Game<?> getCurrentGame() {
        for (Game<?> game : plugin.getGameManager().getGames().values()) {

            if (game.getGameState() != GameState.QUEUE && game.getGameState() != GameState.STOPPED) {
                return game;
            }

        }
        return null;
    }

    public void load() {

        List<String> games = Lists.newArrayList();

        val eventsFolder = new File(plugin.getDataFolder(), "events");

        if (!eventsFolder.exists()) {
            games.addAll(DEFAULT_GAMES);
        } else {
            games.addAll(
                    Arrays.stream(Objects.requireNonNull(eventsFolder.listFiles()))
                            .map(File::getName)
                            .collect(Collectors.toList())
            );
        }

        val economy = plugin.getVaultProvider().provide();
        val clan = plugin.getClanProvider().provide();
        val worldEdit = plugin.getWorldEditProvider().provide();

        for (String gameName : games) {

            val path = "events/" + gameName + "/";
            String internalPath = null;

            if (DEFAULT_GAMES.contains(gameName)) {
                internalPath = language + "/" + path;
            }

            val configProvider = new GameConfigProvider(path, internalPath);

            val config = configProvider.provide(ConfigType.CONFIG);

            val typeName = config.getString("Type");

            val type = Arrays.stream(GameType.values())
                    .filter(it -> it.name().equalsIgnoreCase(typeName))
                    .findFirst();

            if (!type.isPresent()) {
                LoggerHelper.log(Level.SEVERE, "The type &f" + typeName + " is invalid.");
                continue;
            }

            val game = type.get().instantiate(configProvider);

            if (game == null) {
                continue;
            }

            if (game.isEconomyRequired() && !economy.isPresent()) {
                LoggerHelper.log(Level.SEVERE, "The " + game.getName() + " event could not be registred because the Vault was not found.");
                continue;
            }

            if (game instanceof InPerson) {

                val inPersonGame = (InPerson<?>) game;

                if (inPersonGame.isClanRequired() && !clan.isPresent()) {
                    LoggerHelper.log(Level.SEVERE, "The " + game.getName() + " event could not be registred because no clan plugin found.");
                    continue;
                }

                if (inPersonGame.isWorldEditRequired() && !worldEdit.isPresent()) {
                    LoggerHelper.log(Level.SEVERE, "The " + game.getName() + " event could not be registred because the WorldEdit was not found.");
                    continue;
                }

            }

            plugin.getCommandRegistry().register(game, type.get().getCommandClass());

            this.games.put(gameName.toLowerCase(Locale.ROOT), game);
        }
    }


}