package app.miyuki.miyukievents.bukkit.dependency.relocation;

import app.miyuki.miyukievents.bukkit.dependency.DependencyManager;
import app.miyuki.miyukievents.bukkit.dependency.DependencyRegistry;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class RelocationHandler {

    private static final String JAR_RELOCATOR_CLASS = "me.lucko.jarrelocator.JarRelocator";
    private static final String JAR_RELOCATOR_RUN_METHOD = "run";

    private final Constructor<?> jarRelocatorConstructor;
    private final Method jarRelocatorRunMethod;

    @SneakyThrows
    public RelocationHandler(DependencyManager dependencyManager, DependencyRegistry dependencyRegistry) {

        val dependencies = dependencyRegistry.getRelocationDependencies();

        dependencyManager.loadRelocationDependencies();

        val classLoader = dependencyManager.getIsolatedClassLoader(dependencies);

        val jarRelocatorClass = classLoader.loadClass(JAR_RELOCATOR_CLASS);

        this.jarRelocatorConstructor = jarRelocatorClass.getDeclaredConstructor(File.class, File.class, Map.class);
        this.jarRelocatorConstructor.setAccessible(true);

        this.jarRelocatorRunMethod = jarRelocatorClass.getDeclaredMethod(JAR_RELOCATOR_RUN_METHOD);
        this.jarRelocatorRunMethod.setAccessible(true);
    }

    public void handle(Path input, Path output) throws Exception {
        val relocations = Relocation.getRelocations().stream()
                .collect(Collectors.toMap(Relocation::getOldPackage, Relocation::getNewPackage));

        Object relocator = this.jarRelocatorConstructor.newInstance(input.toFile(), output.toFile(), relocations);
        this.jarRelocatorRunMethod.invoke(relocator);
    }

}
