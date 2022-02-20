package app.miyuki.miyukievents.bukkit.reward;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Builder
public class Reward {

    private BigDecimal money;
    private BigDecimal cash;
    private List<String> commands;

    public void execute(@NotNull User user) {
        val playerName = user.getPlayerName();
        val uuid = user.getUuid();

        for (String command : commands) {
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    command.replace("{player}", playerName)
            );
        }

        val plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

        plugin.getVaultProvider().provide().ifPresent(economy -> economy.deposit(uuid, money));

        plugin.getCashProvider().provide().ifPresent(cashAPI -> cashAPI.deposit(uuid, cash));

    }

    public void execute(@NotNull List<User> users) {
        for (User user : users) {
            execute(user);
        }
    }

}
