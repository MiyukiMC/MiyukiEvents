package app.miyuki.miyukievents.bukkit.listener;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.reflection.ReflectionUtils;
import com.google.common.reflect.ClassPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@RequiredArgsConstructor(staticName = "of")
public class ListenerRegistry {

    private static final String LISTENERS = "app.miyuki.miyukievents.bukkit.listener.impl";

    private final MiyukiEvents plugin;

    @SneakyThrows
    public void register() {
        for (ClassPath.ClassInfo classInfo : ReflectionUtils.getClasses(LISTENERS)) {
            val listener = classInfo.load();

            if (!Listener.class.isAssignableFrom(listener))
                return;

            Bukkit.getPluginManager().registerEvents((Listener) listener.getConstructor(MiyukiEvents.class).newInstance(plugin), plugin);
        }
    }

}
