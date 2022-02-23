package app.miyuki.miyukievents.bukkit.dependency.injector;

import org.jetbrains.annotations.NotNull;

import java.net.URLClassLoader;
import java.nio.file.Path;

public abstract class Injector {

    protected final ClassLoader classLoader;

    public Injector(URLClassLoader classLoader) {

        this.classLoader = classLoader;
    }

    public abstract void inject(@NotNull Path dependencyPath);

    public abstract boolean isSupported();


}
