package app.miyuki.miyukievents.bukkit.hook.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public abstract class ChatHook<E extends Event> implements Listener {

    protected final MiyukiEvents plugin;

    public ChatHook(MiyukiEvents plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public abstract void onPlayerChat(E event);

}
