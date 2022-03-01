package app.miyuki.miyukievents.bukkit.dependency;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.dependency.classloader.IsolatedClassLoader;
import app.miyuki.miyukievents.bukkit.dependency.injector.Injector;
import app.miyuki.miyukievents.bukkit.dependency.injector.InjectorFactory;
import app.miyuki.miyukievents.bukkit.dependency.relocation.RelocationHandler;
import app.miyuki.miyukievents.bukkit.storage.StorageType;
import app.miyuki.miyukievents.bukkit.util.async.Async;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

public class DependencyManager {

    private final MiyukiEvents plugin;

    @Getter
    private final DependencyRegistry dependencyRegistry;

    private final Path libraryFolder;

    private final RelocationHandler relocationHandler;

    private final Injector dependencyInjector;

    private final EnumMap<Dependency, Path> loadedDependencies = new EnumMap<>(Dependency.class);

    private final Map<ImmutableSet<Dependency>, IsolatedClassLoader> isolatedClassLoaders = new HashMap<>();

    @SneakyThrows
    public DependencyManager(MiyukiEvents plugin) {
        this.plugin = plugin;

        this.dependencyRegistry = new DependencyRegistry(plugin);

        dependencyInjector = new InjectorFactory(plugin).create();

        libraryFolder = plugin.getDataFolder().toPath().resolve("libraries");

        Files.createDirectories(libraryFolder);

        this.relocationHandler = new RelocationHandler(this, dependencyRegistry);
    }

    public void loadStorageDependencies(StorageType storageType) {
        loadDependencies(dependencyRegistry.getStorageDependencies(storageType));
    }

    public void loadGlobalDependencies() {
        loadDependencies(dependencyRegistry.getGlobalDependencies());
    }

    public void loadRelocationDependencies() {
        loadDependencies(dependencyRegistry.getRelocationDependencies());
    }

    private void loadDependencies(Set<Dependency> dependencies) {

        val latch = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {
            Async.run(() -> {
                try {
                    loadDependency(dependency);
                } catch (Throwable exception) {
                    exception.printStackTrace();
                    LoggerHelper.log(Level.SEVERE, "Unable to load dependency " + dependency.name() + ".");
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

    }

    private void loadDependency(Dependency dependency) throws Throwable {

        val dependencyPath = libraryFolder.resolve(dependency.getFileName() + ".jar");


        if (Files.notExists(dependencyPath)) {

            Throwable lastException = null;

            for (Repository repository : Repository.values()) {

                try {
                    repository.download(dependency, dependencyPath);
                    lastException = null;
                    break;
                } catch (DependencyDownloadException | NoSuchAlgorithmException exception) {
                    lastException = exception;
                }

            }

            if (lastException != null) {
                throw lastException;
            }

        }

        val remappedDependencyPath = remapDependency(dependency, dependencyPath);

        loadedDependencies.put(dependency, remappedDependencyPath);

        if (!dependencyRegistry.isIsolatedDependency(dependency))
            dependencyInjector.inject(remappedDependencyPath);
    }


    synchronized public IsolatedClassLoader getIsolatedClassLoader(Set<Dependency> dependencies) {

        val copyOfDependencies = ImmutableSet.copyOf(dependencies);

        var classLoader = isolatedClassLoaders.get(copyOfDependencies);

        if (classLoader != null)
            return classLoader;

        val urls = dependencies.stream()
                .map(loadedDependencies::get)
                .map(path -> {
                    try {
                        return path.toUri().toURL();
                    } catch (MalformedURLException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .toArray(URL[]::new);

        classLoader = new IsolatedClassLoader(urls);

        isolatedClassLoaders.put(copyOfDependencies, classLoader);

        return classLoader;
    }

    private Path remapDependency(Dependency dependency, Path dependencyFilePath) throws Exception {

        if (dependencyRegistry.isIsolatedDependency(dependency))
            return dependencyFilePath;

        val remappedDependencyPath = libraryFolder.resolve(dependency.getFileName() + "-remapped.jar");

        if (Files.exists(remappedDependencyPath))
            return remappedDependencyPath;

        relocationHandler.handle(dependencyFilePath, remappedDependencyPath);

        return remappedDependencyPath;
    }


}
