package app.miyuki.miyukievents.bukkit.commands;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Game;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class CommandRegistry {

    private final MiyukiEvents plugin;

    @SneakyThrows
    public void register(Game<?> game, Class<?> commandClass) {

        val commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        val commandMap = (CommandMap) commandMapField.get(Bukkit.getPluginManager());

        if (!(GameCommand.class.isAssignableFrom(commandClass)))
            throw new IllegalArgumentException();

        val configRoot = game.getConfig().getRoot();

        val commandName = configRoot.node("Command", "Names").getList(String.class, ArrayList::new);

        if (commandName.isEmpty())
            return;

        val command = (GameCommand) commandClass
                .getConstructor(MiyukiEvents.class, Game.class, String.class, List.class)
                .newInstance(plugin, game, commandName.get(0), commandName);

        commandMap.register("miyukievents", command);
    }

}