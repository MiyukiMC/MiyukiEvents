package app.miyuki.miyukievents.bukkit.game.manager;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.*;
import app.miyuki.miyukievents.bukkit.game.inperson.InPerson;
import app.miyuki.miyukievents.bukkit.game.queue.GameQueue;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GameManager {

    private static final ImmutableList<String> DEFAULT_GAMES = ImmutableList.of(
            "word",
            "math",
            "lottery",
            "pool",
            "fastquiz",
            "jackpot",
            "fastclick",
            "archer",
            "auction"
    );

    private final MiyukiEvents plugin;
    private final String language;

    @Getter
    private final GameQueue queue;

    @Getter
    private final GameRegistry gameRegistry;

    @Setter
    @Nullable
    private Game<?> currentGame = null;

    @Getter
    private final Map<String, Game<?>> games = Maps.newHashMap();

    public GameManager(@NotNull MiyukiEvents plugin, @NotNull String language) {
        this.plugin = plugin;
        this.language = language;

        this.queue = new GameQueue(plugin, this);
        this.gameRegistry = new GameRegistry();

        this.gameRegistry.load();
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

            var path = "events/" + gameName + "/";
            String internalPath = null;

            if (DEFAULT_GAMES.contains(gameName)) {
                internalPath = language + "/" + path;
                path = plugin.getDataFolder() + "/" + path;
            }

            val config = new Config(path + "config.yml", internalPath + "config.yml");
            val configRoot = config.getRoot();

            val typeName = configRoot.node("Type").getString();

            val typeClass = gameRegistry.getGames().stream()
                    .filter(it -> it.getAnnotation(GameInfo.class).typeName().equalsIgnoreCase(typeName))
                    .findFirst();

            if (!typeClass.isPresent()) {
                LoggerHelper.log(Level.SEVERE, "The type " + typeName + " is invalid.");
                continue;
            }

            val messages = new Config(path + "messages.yml", internalPath + "messages.yml");
            val data = new Config(path + "data.yml", internalPath + "data.yml");

            Game<?> game;
            try {
                game = (Game<?>) typeClass.get().getConstructor(Config.class, Config.class, Config.class).newInstance(config, messages, data);
            } catch (Exception exception) {
                LoggerHelper.log(Level.SEVERE, "The " + typeName + " event cannot be instantiated.");
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

            val commandClass = typeClass.get().getAnnotation(GameInfo.class).commandClass();

            plugin.getCommandRegistry().register(game, commandClass);

            this.games.put(gameName.toLowerCase(Locale.ROOT), game);
        }
    }

    @Nullable
    public Game<?> getCurrentGame() {
        if (this.currentGame != null) {
            val gameState = this.currentGame.getGameState();
            if (gameState == GameState.STOPPED || gameState == GameState.QUEUE)
                this.currentGame = null;
        }
        return this.currentGame;
    }


}