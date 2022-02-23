package app.miyuki.miyukievents.bukkit.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Dependency {


    ASM(
            "org{}ow2{}asm",
            "asm",
            "9.2"
    ),
    ASM_COMMONS(
            "org{}ow2{}asm",
            "asm-commons",
            "9.2"
    ),
    JAR_RELOCATOR(
            "me{}lucko",
            "jar-relocator",
            "1.5"
    ),
    MARIADB_DRIVER(
            "org{}mariadb{}jdbc",
            "mariadb-java-client",
            "3.0.3"
    ),
    MYSQL_DRIVER(
            "mysql",
            "mysql-connector-java",
            "8.0.28"
    ),
    H2_DRIVER(
            "com{}h2database",
            "h2",
            "2.1.210"
    ),
    SQLITE_DRIVER(
            "org{}xerial",
            "sqlite-jdbc",
            "3.36.0.3"
    ),
    JEDIS(
            "redis{}clients",
            "jedis",
            "4.1.1"
    ),
    HIKARI(
            "com{}zaxxer",
            "HikariCP",
            "4.0.3"
    ),
    CAFFEINE(
            "com{}github{}ben-manes{}caffeine",
            "caffeine",
            "2.9.3"
    ),
    SLF4J_SIMPLE(
            "org{}slf4j",
            "slf4j-simple",
            "1.7.36"
    ),
    SLF4J_API(
            "org{}slf4j",
            "slf4j-api",
            "1.7.36"
    );

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    private static final String FILE_NAME = "%s-%s";

    private final String groupId;
    private final String artifactId;
    private final String version;


    public String getMavenRepositoryPath() {
        return String.format(MAVEN_FORMAT,
                groupId.replace("{}", "/"),
                artifactId,
                version,
                artifactId,
                version
        );
    }

    public String getFileName() {
        return String.format(FILE_NAME, artifactId, version);
    }

    public void validateChecksum() {

    }

}