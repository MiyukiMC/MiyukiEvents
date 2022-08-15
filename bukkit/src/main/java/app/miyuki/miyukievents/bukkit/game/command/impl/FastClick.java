package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.command.GenericCommandCommand;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@GameInfo(typeName = "FastClick", commandClass = GenericCommandCommand.class)
public class FastClick extends Command<User> {

    // Used to verify json
    private String id;

    public FastClick(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length != 1)
            return;

        if (!args[0].equals(id)) {
            return;
        }

        if (permission != null && !player.hasPermission(permission)) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermission");
            return;
        }

        if (!checkCost(player)) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoMoney");
            return;
        }

        val uniqueId = player.getUniqueId();

        val user = plugin.getUserRepository().findById(uniqueId).get(); // null check

        this.onWin(user);
    }

    @SneakyThrows
    @Override
    public void start() {
        this.setGameState(GameState.STARTED);
        this.id = RandomUtils.generateRandomString(15);

        val configRoot = config.getRoot();

        List<String> messagesPaths = Lists.newArrayList();

        for (CommentedConfigurationNode node : configRoot.node("Messages").childrenMap().values()) {

            if (node.isList()) {
                messagesPaths.add(String.join("<reset><newline>", node.getList(String.class, ArrayList::new)));
            } else {
                messagesPaths.add(node.getString());
            }

        }

        var message = Objects.requireNonNull(RandomUtils.getRandomElement(messagesPaths)).replace("{id}", this.id);

        plugin.getAdventure().all().sendMessage(ChatUtils.colorize(message));

        val expireTime = configRoot.node("ExpireTime").getInt();

        this.schedulerManager.runAsync(expireTime * 20L, () -> {
            this.messageDispatcher.globalDispatch("NoWinner");
            this.stop();
        });
    }

    @Override
    public void stop() {
        this.setGameState(GameState.STOPPED);
        this.schedulerManager.cancel();
    }

    @Override
    public void onWin(User user) {
        this.stop();
        this.giveReward(user);

        this.messageDispatcher.globalDispatch("Win", message -> message
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