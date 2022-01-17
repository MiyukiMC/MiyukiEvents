package app.miyuki.bukkit.listener;

import app.miyuki.bukkit.MiyukiEvents;
import com.google.common.reflect.ClassPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@RequiredArgsConstructor(staticName = "of")
public class ListenerRegistry {

    private final MiyukiEvents plugin;

    @SneakyThrows
    public void register() {
        ClassPath classPath = ClassPath.from(plugin.getClass().getClassLoader());

        for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive("app.miyuki.bukkit.listener.impl")) {
            val listener = classInfo.load().getConstructor(MiyukiEvents.class).newInstance(plugin);
            if (listener instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) listener, plugin);
            }
        }

    }

}