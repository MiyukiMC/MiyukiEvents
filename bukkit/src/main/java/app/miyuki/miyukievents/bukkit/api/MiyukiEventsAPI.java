package app.miyuki.miyukievents.bukkit.api;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.api.exception.InvalidGameException;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import app.miyuki.miyukievents.bukkit.game.queue.GameQueue;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.user.UserRepository;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class MiyukiEventsAPI {

    private static final MiyukiEvents PLUGIN = JavaPlugin.getPlugin(MiyukiEvents.class);

    /**
     * The game manager.
     */
    private final GameManager gameManager = PLUGIN.getGameManager();

    /**
     * The user repository.
     */
    private final UserRepository userRepository = PLUGIN.getUserRepository();

    /**
     * Gets a user in our data store.
     *
     * @param playerName
     *        The name of the player you want to get.
     *
     * @return {@link java.util.Optional} with the user.
     */
    public Optional<User> getUser(@NotNull String playerName) {
        return userRepository.findByName(playerName);
    }

    /**
     * Gets a user in our data store.
     *
     * @param uniqueId
     *        The {@link java.util.UUID} of the player you want to get.
     *
     * @return {@link java.util.Optional} with the user.
     */
    public Optional<User> getUser(@NotNull UUID uniqueId) {
        return userRepository.findById(uniqueId);
    }

    /**
     * Starts a game.
     *
     * @param gameName
     *        The name of the game you want to start.
     *
     * @throws {@link app.miyuki.miyukievents.bukkit.api.exception.InvalidGameException}
     *         If the game doesn't exist.
     */
    public void startGame(@NotNull String gameName) {
        val game = this.getGame(gameName);

        if (!game.isPresent())
            throw new InvalidGameException("The game you entered is invalid");

        game.get().start();
    }

    /**
     * Get the game queue.
     *
     * @return The {@link app.miyuki.miyukievents.bukkit.game.queue.GameQueue}.
     */
    public GameQueue getGameQueue() {
        return this.gameManager.getQueue();
    }

    /**
     * Gets a game.
     *
     * @param gameName
     *        The name of the game you want.
     *
     * @return {@link java.util.Optional} with the game.
     */
    public Optional<Game<?>> getGame(@NotNull String gameName) {
        return Optional.empty();
    }

    /**
     * Gets the current game.
     *
     * @return {@link java.util.Optional} with the current game.
     */
    public Optional<Game<?>> getCurrentGame() {
        return Optional.ofNullable(gameManager.getCurrentGame());
    }

    /**
     * Check if there is any game happening.
     *
     * @return if there is a game happening.
     */
    public boolean isGameHappening() {
        return gameManager.getCurrentGame() != null;
    }

}
