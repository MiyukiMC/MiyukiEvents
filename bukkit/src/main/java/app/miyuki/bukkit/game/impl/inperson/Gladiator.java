package app.miyuki.bukkit.game.impl.inperson;

import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameState;
import app.miyuki.bukkit.game.InPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Gladiator extends Game<List<Player>> implements InPerson {

    public Gladiator(@NotNull ConfigProvider configProvider) {
        super(configProvider);
        this.reward = getPlugin().getRewardAdapter().adapt(
                getConfigProvider().provide(ConfigType.CONFIG).getConfigurationSection("Reward")
        );
    }

    @Override
    public String getTypeName() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Type");
    }

    @Override
    public String getName() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Name");
    }

    @Override
    public String getPermission() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Permission");
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(List<Player> players) {
        giveReward(players);
    }

    @Override
    public void giveReward(List<Player> players) {
        for (Player player : players) {
            this.reward.execute(player);
        }
    }

    @Override
    public void join(Player player) {

    }

    @Override
    public void leave(Player player) {

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {

    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }

}
