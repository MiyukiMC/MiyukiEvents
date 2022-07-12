package app.miyuki.miyukievents.bukkit.commands.impl.command.auction;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.commands.evaluator.GenericStartConditionEvaluator;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.impl.Auction;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AuctionStartSubCommand extends SubCommand {

    private final Game<?> game;
    private final MessageDispatcher messageDispatcher;
    private final MessageDispatcher globalMessageDispatcher;
    private final GameConfigProvider configProvider;

    public AuctionStartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
        this.globalMessageDispatcher = plugin.getGlobalMessageDispatcher();
        this.configProvider = configProvider;
    }

    @Override
    public List<String> getAliases() {
        return this.configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Start.Names");
    }

    @Override
    @Nullable
    public String getPermission() {
        return this.configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Start.Permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 1) {
            this.messageDispatcher.dispatch(sender, "HelpAdmin");
            return false;
        }

        if (!GenericStartConditionEvaluator.of(globalMessageDispatcher).evaluate(sender, game.getGameState()))
            return false;

        val auctionGame = (Auction) this.game;
        auctionGame.setupAuctionItems();

        val auctionItem = auctionGame.getAuctionItemByName(args[0]);

        if (auctionItem == null) {
            this.messageDispatcher.dispatch(sender, "InvalidAuctionItem");
            return false;
        }

        this.game.setGameState(GameState.QUEUE);
        auctionGame.setAuctionItem(auctionItem);
        this.plugin.getGameManager().getQueue().register(game);

        this.globalMessageDispatcher.dispatch(sender, "GameAddedToQueue");
        return true;
    }


}
