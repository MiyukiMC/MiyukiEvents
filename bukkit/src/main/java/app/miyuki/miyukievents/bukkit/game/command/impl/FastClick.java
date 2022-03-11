package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.command.FastClickCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Lists;
import lombok.val;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@GameInfo(typeName = "FastClick", commandClass = FastClickCommand.class)
public class FastClick extends Command<User> {

    private String id;

    public FastClick(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (!args[0].equals(id)) {
            return;
        }

        if (!player.hasPermission(getPermission())) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermission");
            return;
        }

        if (!checkCost(player)) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoMoney");
            return;
        }


        onWin(plugin.getUserRepository().findById(player.getUniqueId()));
    }

    @Override
    public void start() {
        setGameState(GameState.STARTED);
        this.id = RandomUtils.generateRandomString(15);

        val config = configProvider.provide(ConfigType.CONFIG);

        val messagesPaths = Lists.newArrayList(config.getConfigurationSection("Messages").getKeys(false));

        val messagePath = RandomUtils.getRandomElement(messagesPaths);

        String message;
        val messagesSection = config.getConfigurationSection("Messages");

        if (messagesSection.isList(messagePath)) {

            message = String.join("<newline>", messagesSection.getStringList(messagePath));

        } else {

            message = messagesSection.getString(messagePath);

        }

        val miniMessage = MiniMessage.miniMessage();

        message = message.replace("{id}", this.id);

        plugin.getAdventure().all().sendMessage(miniMessage.deserialize(plugin.getTextColorAdapter().adapt(message)).asComponent());

        val expireTime = configProvider.provide(ConfigType.CONFIG).getInt("ExpireTime");

        schedulerManager.runAsync(expireTime * 20L, () -> {
            messageDispatcher.globalDispatch("NoWinner");
            stop();
        });
    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
        schedulerManager.cancel();
    }

    @Override
    public void onWin(User user) {
        stop();
        giveReward(user);

        messageDispatcher.globalDispatch("Win", message -> message
                .replace("{winner}", user.getPlayerName()));
    }

    @Override
    protected void giveReward(User user) {
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

}