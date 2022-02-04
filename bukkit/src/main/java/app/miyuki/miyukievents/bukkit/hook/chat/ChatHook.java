package app.miyuki.miyukievents.bukkit.hook.chat;


import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@RequiredArgsConstructor(staticName = "of")
public class ChatHook {

    private final MiyukiEvents plugin;

    @SneakyThrows
    public void hook() {

        val pluginManager = Bukkit.getPluginManager();

        Class<?> clazz = null;

        if (pluginManager.getPlugin("nChat") != null) {
            clazz = NChat.class;
        } else if (pluginManager.getPlugin("LegendChat") != null) {
            clazz = LegendChat.class;
        } else if (pluginManager.getPlugin("UltimateChat") != null) {
            clazz = UltimateChat.class;
        }

        if (clazz == null)
            return;

        Bukkit.getPluginManager().registerEvents((Listener) clazz.getConstructor(MiyukiEvents.class).newInstance(plugin), plugin);

    }


}
