package app.miyuki.miyukievents.bukkit.game;

import org.bukkit.entity.Player;

public interface Command {

    void onCommand(Player player, String[] args);

}
