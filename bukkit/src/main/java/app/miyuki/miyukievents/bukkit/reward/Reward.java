package app.miyuki.miyukievents.bukkit.reward;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@Builder
public class Reward {

    private double money;
    private double cash;
    private List<String> commands;

    public void execute(@NotNull Player player) {
        val playerName = player.getName();

        for (String command : commands) {
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    command.replace("{player}", playerName)
            );
        }

        val plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

        if (NumberEvaluator.isValid(cash)) {
            val cashAPI = plugin.getCashProvider().provide();

            if (cashAPI == null)
                return;

            cashAPI.deposit(playerName, cash);
        }

        if (NumberEvaluator.isValid(money)) {
            val economy = plugin.getVaultProvider().provide();

            if (economy == null)
                return;

            economy.depositPlayer(player, money);
        }
    }

}
