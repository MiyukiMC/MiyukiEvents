package app.miyuki.miyukievents.bukkit.commands.impl.command.auction;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.commands.evaluator.GenericStartConditionEvaluator;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.impl.Auction;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AuctionStartSubCommand extends SubCommand {

    private final Auction game;

    private final MessageDispatcher messageDispatcher;

    private final MessageDispatcher globalMessageDispatcher;

    public AuctionStartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Auction game,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
        this.globalMessageDispatcher = plugin.getGlobalMessageDispatcher();
    }

    @SneakyThrows
    @Override
    public List<String> getAliases() {
        return game.getConfig().getRoot().node("SubCommands", "Start", "Names").getList(String.class, ArrayList::new);
    }

    @Override
    public @Nullable String getPermission() {
        return game.getConfig().getRoot().node("SubCommands", "Start", "Permission").getString();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (!GenericStartConditionEvaluator.evaluate(globalMessageDispatcher, sender, game.getGameState()))
            return false;


        val auctionGame = (Auction) this.game;
        auctionGame.setupAuctionItems();

        val auctionItem = args.length == 1 ? game.getAuctionItemByName(args[0]) : auctionGame.getRandomAuctionItem();

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
