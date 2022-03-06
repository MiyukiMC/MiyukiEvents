package app.miyuki.miyukievents.bukkit.dependency.relocation;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Relocation {

    private static final String PACKAGE = "app.miyuki.miyukievents.libs.";

    private static final ImmutableSet<Relocation> RELOCATIONS = ImmutableSet.of(
            Relocation.of("com{}google{}errorprone{}annotations", "errorprone.annotations"),
            Relocation.of("com{}mysql{}cj", "mysql"),
            Relocation.of("org{}apache{}commons.pool2", "pool2"),
            Relocation.of("org{}h2", "h2"),
            Relocation.of("de{}tr7zw{}changeme{}nbtapi", "nbtapi"),
            Relocation.of("de{}tr7zw{}annotations", "nbtapi.annotations"),
            Relocation.of("org{}json", "json"),
            Relocation.of("org{}mariadb{}jdbc", "mariadb"),
            Relocation.of("redis{}clients{}jedis", "jedis"),
            Relocation.of("com{}google{}protobuf", "protobuf"),
            Relocation.of("com{}zaxxer{}hikari", "hikari"),
            Relocation.of("net{}kyori{}examination", "kyori.examination"),
            Relocation.of("net{}kyori{}adventure", "kyori.adventure"),
            Relocation.of("org{}slf4j", "slf4j")
    );

    private final String oldPackage;
    private final String newPackage;

    public static Relocation of(String currentPackage, String id) {
        return new Relocation(currentPackage.replace("{}", "."), PACKAGE + id);
    }

    public static ImmutableSet<Relocation> getRelocations() {
        return RELOCATIONS;
    }


}