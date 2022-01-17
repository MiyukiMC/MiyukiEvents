package app.miyuki.bukkit.reward;

import app.miyuki.bukkit.MiyukiEvents;
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

    private Double money;
    private Double cash;
    private List<String> commands;

    public void execute(@NotNull Player player) {
        for (String command : commands) {
            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    command.replace("{player}", player.getName())
            );
        }

        val plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

        // deposit cash, money
    }

}
