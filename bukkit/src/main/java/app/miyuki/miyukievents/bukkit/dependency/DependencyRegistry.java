package app.miyuki.miyukievents.bukkit.dependency;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.StorageType;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import lombok.AllArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
public class DependencyRegistry {

    private static final ListMultimap<StorageType, Dependency> STORAGE_DEPENDENCIES = ImmutableListMultimap.<StorageType, Dependency>builder()
            .putAll(StorageType.MARIADB, Dependency.MARIADB_DRIVER, Dependency.SLF4J_API, Dependency.SLF4J_SIMPLE, Dependency.HIKARI)
            .putAll(StorageType.MYSQL, Dependency.MYSQL_DRIVER, Dependency.SLF4J_API, Dependency.SLF4J_SIMPLE, Dependency.HIKARI)
            .putAll(StorageType.SQLITE, Dependency.SQLITE_DRIVER)
            .putAll(StorageType.H2, Dependency.H2_DRIVER)
            .build();


    private static final Set<Dependency> RELOCATION_DEPENDENCIES = ImmutableSet.of(Dependency.ASM, Dependency.ASM_COMMONS, Dependency.JAR_RELOCATOR);

    private final MiyukiEvents plugin;

    public Set<Dependency> getRelocationDependencies() {
        return RELOCATION_DEPENDENCIES;
    }

    public Set<Dependency> getStorageDependencies(StorageType storageType) {
        Set<Dependency> dependencies = new LinkedHashSet<>(STORAGE_DEPENDENCIES.get(storageType));

        if (plugin.getGlobalConfig().getBoolean("Bungeecord.Enabled")) {
            dependencies.add(Dependency.JEDIS);
            dependencies.add(Dependency.SLF4J_API);
            dependencies.add(Dependency.SLF4J_SIMPLE);
        }

        return dependencies;
    }

    public Set<Dependency> getGlobalDependencies() {
        return ImmutableSet.of(
                Dependency.CAFFEINE
        );
    }

    public boolean isRelocationDependency(Dependency dependency) {
        return RELOCATION_DEPENDENCIES.contains(dependency);
    }

}
