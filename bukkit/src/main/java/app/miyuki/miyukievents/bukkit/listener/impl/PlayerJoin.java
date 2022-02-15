package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.user.UserFactory;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerJoin {

    private final MiyukiEvents plugin;
    private final UserFactory userFactory;

    public PlayerJoin(MiyukiEvents plugin) {

        this.plugin = plugin;
        userFactory = new UserFactory(plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {

        val users = plugin.getStorage().getUsers();

        val uuid = event.getUniqueId();

        val user = users.get(uuid.toString());

        if (user == null) {



            return;
        }


    }

}
