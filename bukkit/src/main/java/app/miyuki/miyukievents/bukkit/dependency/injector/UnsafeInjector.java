package app.miyuki.miyukievents.bukkit.dependency.injector;

import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;

public class UnsafeInjector extends Injector {

    private static final sun.misc.Unsafe UNSAFE;

    static {
        sun.misc.Unsafe unsafe;
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        } catch (Exception ignored) {
            unsafe = null;
        }
        UNSAFE = unsafe;
    }

    private final Collection<URL> unopenedURLs;
    private final Collection<URL> pathURLs;

    public UnsafeInjector(URLClassLoader classLoader) {
        super(classLoader);

        Collection<URL> unopenedURLs;
        Collection<URL> pathURLs;
        try {
            Object ucp = fetchField(URLClassLoader.class, classLoader, "ucp");
            unopenedURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "unopenedUrls");
            pathURLs = (Collection<URL>) fetchField(ucp.getClass(), ucp, "path");
        } catch (Throwable e) {
            unopenedURLs = null;
            pathURLs = null;
        }
        this.unopenedURLs = unopenedURLs;
        this.pathURLs = pathURLs;
    }

    @SneakyThrows
    @Override
    public void inject(@NotNull Path dependencyPath) {

        val url = dependencyPath.toUri().toURL();

        this.unopenedURLs.add(url);
        this.pathURLs.add(url);
    }

    @Override
    public boolean isSupported() {
        return UNSAFE != null;
    }

    private static Object fetchField(final Class<?> clazz, final Object object, final String name) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(name);
        long offset = UNSAFE.objectFieldOffset(field);
        return UNSAFE.getObject(object, offset);
    }

}
