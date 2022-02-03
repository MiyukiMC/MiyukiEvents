package app.miyuki.miyukievents.bukkit.listener;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

@RequiredArgsConstructor(staticName = "of")
public class ListenerRegistry {

    private static final String PACKAGE = "app.miyuki.miyukievents.bukkit.";

    private static final ImmutableList<String> PACKAGES = ImmutableList.of(
            "listener.impl",
            "hook.chat"
    );

    private final MiyukiEvents plugin;

    @SneakyThrows
    public void register() {
        ClassPath classPath = ClassPath.from(plugin.getClass().getClassLoader());

        for (String packagePath : PACKAGES) {
            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(PACKAGE + packagePath)) {
                val listener = classInfo.load();

                if (!Listener.class.isAssignableFrom(listener))
                    return;

                Bukkit.getPluginManager().registerEvents((Listener) listener.getConstructor(MiyukiEvents.class).newInstance(plugin), plugin);
            }
        }

    }

}
