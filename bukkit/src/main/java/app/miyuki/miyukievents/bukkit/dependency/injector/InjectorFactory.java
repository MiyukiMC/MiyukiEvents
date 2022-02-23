package app.miyuki.miyukievents.bukkit.dependency.injector;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;

import java.net.URLClassLoader;
import java.util.logging.Level;

@AllArgsConstructor
public class InjectorFactory {

    private final MiyukiEvents plugin;

    public Injector create() {
        val classLoader = (URLClassLoader) plugin.getClass().getClassLoader();

        val urlClassLoaderInjector = new URLClassLoaderInjector(classLoader);

        if (urlClassLoaderInjector.isSupported())
            return urlClassLoaderInjector;

        val unsafeInjector = new UnsafeInjector(classLoader);

        if (unsafeInjector.isSupported())
            return unsafeInjector;

        LoggerHelper.log(Level.SEVERE, "The dependency system is not working properly.");
        Bukkit.getPluginManager().disablePlugin(plugin);
        return null;
    }

}
