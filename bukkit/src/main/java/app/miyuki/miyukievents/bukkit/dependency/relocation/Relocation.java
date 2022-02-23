package app.miyuki.miyukievents.bukkit.dependency.relocation;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Relocation {

    private static final ImmutableSet<Relocation> RELOCATIONS = ImmutableSet.of(
            Relocation.of("com{}google{}errorprone{}annotations", "errorprone.annotations"),
            Relocation.of("com{}google{}gson", "gson"),
            Relocation.of("com{}mysql{}cj", "mysql"),
            Relocation.of("org{}apache{}commons.pool2", "pool2"),
            Relocation.of("org{}checkerframework", "checkerframework"),
            Relocation.of("org{}h2", "h2"),
            Relocation.of("org{}json", "json"),
            Relocation.of("org{}mariadb{}jdbc", "mariadb"),
            Relocation.of("org{}sqlite", "sqlite"),
            Relocation.of("redis{}clients{}jedis", "jedis"),
            Relocation.of("com{}google{}protobuf", "protobuf"),
            Relocation.of("com{}github{}benmanes{}caffeine", "caffeine"),
            Relocation.of("com{}zaxxer{}hikari", "hikari"),
            Relocation.of("org{}slf4j", "slf4j")
    );



    public static ImmutableSet<Relocation> getRelocations() {
        return RELOCATIONS;
    }

    private static final String PACKAGE = "app.miyuki.miyukievents.libs.";

    public static Relocation of(String currentPackage, String id) {
        return new Relocation(currentPackage.replace("{}", "."), PACKAGE + id);
    }

    private final String oldPackage;
    private final String newPackage;

}