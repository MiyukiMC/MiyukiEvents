package app.miyuki.miyukievents.bukkit.dependency.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class IsolatedClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public IsolatedClassLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader().getParent());
    }


}
