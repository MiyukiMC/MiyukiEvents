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
                Dependency.ADVENTURE_EXAMINATION,
                Dependency.ADVENTURE_EXAMINATION_STRING,
                Dependency.ADVENTURE_KEY,
                Dependency.ADVENTURE_API,
                Dependency.ADVENTURE_MINIMESSAGE,
                Dependency.ADVENTURE_BUKKIT,
                Dependency.ADVENTURE_PLATAFORM_API,
                Dependency.ADVENTURE_FACET,
                Dependency.ADVENTURE_SERIALIZER_BUNGEECORD,
                Dependency.ADVENTURE_SERIALIZER_GSON,
                Dependency.ADVENTURE_GSON_LEGACY_IMPL,
                Dependency.ADVENTURE_SERIALIZER_LEGACY,
                Dependency.ADVENTURE_NBT
        );
    }

    public boolean isIsolatedDependency(Dependency dependency) {
        return RELOCATION_DEPENDENCIES.contains(dependency) || dependency == Dependency.SQLITE_DRIVER;
    }

}
