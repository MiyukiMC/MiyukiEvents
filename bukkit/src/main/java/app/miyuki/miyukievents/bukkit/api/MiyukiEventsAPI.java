package app.miyuki.miyukievents.bukkit.api;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.user.UserRepository;
import com.google.common.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MiyukiEventsAPI {

    private static final MiyukiEvents PLUGIN = JavaPlugin.getPlugin(MiyukiEvents.class);

    private final JavaPlugin accessorPlugin;

    private final GameManager gameManager = PLUGIN.getGameManager();
    private final UserRepository userRepository = PLUGIN.getUserRepository();

    public MiyukiEventsAPI(JavaPlugin accessorPlugin) {
        this.accessorPlugin = accessorPlugin;
    }

    /**
     * Documentation
     */
    public Optional<User> getUser(@NotNull String playerName) {
        return userRepository.findByName(playerName);
    }

    /**
     * Documentation
     */
    public Optional<User> getUser(@NotNull UUID uniqueId) {
        return userRepository.findById(uniqueId);
    }

    /**
     * Documentation
     * */
    public Optional<Game<?>> getLastGame() {
        // It is necessary?
        return Optional.ofNullable(gameManager.getLastGame());
    }

    /**
     * Documentation
     */
    public void startGame(String gameName) {

    }

    /**
     * Documentation
     */
    public Optional<Game<?>> getCurrentGame() {
        return Optional.ofNullable(gameManager.getCurrentGame());
    }

    /**
     * Documentation
     */
    public Optional<String> getGameNameOfCurrentGame() {
        return Optional.ofNullable(gameManager.getCurrentGame().getName());
    }

    /**
     * Documentation
     */
    public void stopCurrentGame() {
        val game = gameManager.getCurrentGame();

        // maybe throws a Runtime Exception if game is null
        if (game == null)
            return;

        game.stop();
    }

    /**
     * Documentation
     */
    public boolean isGameHappening() {
        return gameManager.getCurrentGame() != null;
    }

}
