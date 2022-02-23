package app.miyuki.miyukievents.bukkit.dependency.injector;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class URLClassLoaderInjector extends Injector {

    private static final Method ADD_URL_METHOD;

    static {
        Method addUrlMethod;
        try {
            addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);
        } catch (Exception e) {
            addUrlMethod = null;
        }
        ADD_URL_METHOD = addUrlMethod;
    }

    public URLClassLoaderInjector(URLClassLoader classLoader) {
        super(classLoader);
    }

    @SneakyThrows
    @Override
    public void inject(@NotNull Path dependencyPath) {
        ADD_URL_METHOD.invoke(classLoader, dependencyPath.toUri().toURL());
    }

    @Override
    public boolean isSupported() {
        return ADD_URL_METHOD != null;
    }


}
