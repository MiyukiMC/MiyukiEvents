package app.miyuki.miyukievents.bukkit.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import java.util.Arrays;
import java.util.Base64;


@AllArgsConstructor
@Getter
public enum Dependency {


    ASM(
            "org{}ow2{}asm",
            "asm",
            "9.2",
            "udT+TXGTjfOIOfDspCqqpkz4sxPWeNoDbwyzyhmbR/U="
    ),
    ASM_COMMONS(
            "org{}ow2{}asm",
            "asm-commons",
            "9.2",
            "vkzlMTiiOLtSLNeBz5Hzulzi9sqT7GLUahYqEnIl4KY="
    ),
    JAR_RELOCATOR(
            "me{}lucko",
            "jar-relocator",
            "1.5",
            "0D6eM99gKpEYFNDydgnto3Df0ygZGdRVqy5ahtj0oIs="
    ),
    ITEM_NBT_API(
            "de{}tr7zw",
            "item-nbt-api",
            "2.9.2",
            "c7LsiYbsUQSxYEJrbhpj2olMXw99RyAZKsOtrJIMess="
    ),
    MARIADB_DRIVER(
            "org{}mariadb{}jdbc",
            "mariadb-java-client",
            "3.0.3",
            "YTCGoKIPF3389eIn9RknK8a+iL3kAR3g8jxTMjGnrgU="
    ),
    MYSQL_DRIVER(
            "mysql",
            "mysql-connector-java",
            "8.0.28",
            "fAJK7/HBBjV210RTUT+d5kR9jmJNF/jifzCi6XaIxsk="
    ),
    H2_DRIVER(
            "com{}h2database",
            "h2",
            "2.1.210",
            "7cVymZJil/2TFeBN51+FOMTLX+l/09oqHlzuakyYtc0="
    ),
    SQLITE_DRIVER(
            "org{}xerial",
            "sqlite-jdbc",
            "3.36.0.3",
            "rzozdjkeGGoP7WPs1BS3Kogr9FJme0kKC+Or+FtjfT8="
    ),
    JEDIS(
            "redis{}clients",
            "jedis",
            "4.1.1",
            "uDKQd9R5VecnyQrQq3Bsj5+lwh5ahMM+V9TwOFD2syo="
    ),
    HIKARI(
            "com{}zaxxer",
            "HikariCP",
            "4.0.3",
            "fAJK7/HBBjV210RTUT+d5kR9jmJNF/jifzCi6XaIxsk="
    ),
    ADVENTURE_API(
            "net{}kyori",
            "adventure-api",
            "4.10.0",
            "390TP+6R5SjJxIrV37jVraevBiq+zOch6f4WlAf3mJ4="
    ),
    ADVENTURE_PLATAFORM_API(
            "net{}kyori",
            "adventure-platform-api",
            "4.1.0",
            "x0QUJljwbSlLVQqb/HE8Rk5FPO+4CEZ14yfeF1gVaeg="
    ),
    ADVENTURE_FACET(
            "net{}kyori",
            "adventure-platform-facet",
            "4.1.0",
            "gZgYcDNnayEQE55tflLcD9Zgc5CFzuHmaE8DrJX9eHo="
    ),
    ADVENTURE_EXAMINATION(
            "net{}kyori",
            "examination-api",
            "1.3.0",
            "ySN//ssFQo9u/4YhYkascM4LR7BMCOp8o1Ag/eV/hJI="
    ),
    ADVENTURE_EXAMINATION_STRING(
            "net{}kyori",
            "examination-string",
            "1.3.0",
            "fQH8JaS7OvDhZiaFRV9FQfv0YmIW6lhG5FXBSR4Va4w="
    ),
    ADVENTURE_KEY(
            "net{}kyori",
            "adventure-key",
            "4.10.0",
            "nleXqkKbWgpI7zGkRDx02iEE/6sjjdc6GafasX2zLYY="
    ),
    ADVENTURE_BUKKIT(
            "net{}kyori",
            "adventure-platform-bukkit",
            "4.1.0",
            "LSHIyaMBdK95zODuPp1nnAfCu13lNdkRVCETBJPOL50="
    ),
    ADVENTURE_MINIMESSAGE(
            "net{}kyori",
            "adventure-text-minimessage",
            "4.10.0",
            "qJJbZyZtqmBscTmNBIffSJyAD9tICv9lWv3QmLxZEXQ="
    ),
    ADVENTURE_NBT(
            "net{}kyori",
            "adventure-nbt",
            "4.10.0",
            "X0NfIt1zfQRTdKPQngRVjlZBd5I+CK6fpJ6O8tZiCEg="
    ),
    ADVENTURE_SERIALIZER_BUNGEECORD(
            "net{}kyori",
            "adventure-text-serializer-bungeecord",
            "4.1.0",
            "v7CwvYDAPVgkhmi6QR3IP02mILywNnPHNK8lDx13kKI="
    ),
    ADVENTURE_SERIALIZER_GSON(
            "net{}kyori",
            "adventure-text-serializer-gson",
            "4.10.0",
            "RsjEsrCjv3fHARYRo1pRMzALYDQlZvn7IAWZTzg+FSk="
    ),
    ADVENTURE_GSON_LEGACY_IMPL(
            "net{}kyori",
            "adventure-text-serializer-gson-legacy-impl",
            "4.10.0",
            "tArMVVb83A9tL66phdIo6QBdEMeQvslFarbEHv5hcXU="
    ),
    ADVENTURE_SERIALIZER_LEGACY(
            "net{}kyori",
            "adventure-text-serializer-legacy",
            "4.10.0",
            "BrU7skaoTZ6QxthUckw97bLymtyvD/6ojmqIBSgIye0="
    ),
    SLF4J_SIMPLE(
            "org{}slf4j",
            "slf4j-simple",
            "1.7.36",
            "Lzm+2UPWJN+o9BAtBXEoOhCHC2qjbxl6ilBvFHAQwQ8="
    ),
    SLF4J_API(
            "org{}slf4j",
            "slf4j-api",
            "1.7.36",
            "0+9XXj5JeWeNwBvx3M5RAhSTtNEft/G+itmCh3wWocA="
    );

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    private static final String FILE_NAME = "%s-%s";

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String snapshotId;
    private final String checksum;

    Dependency(String groupId, String artifactId, String version, String checksum) {
        this(groupId, artifactId, version, null, checksum);
    }

    public String getMavenRepositoryPath() {
        return String.format(MAVEN_FORMAT,
                groupId.replace("{}", "/"),
                artifactId,
                version,
                artifactId,
                snapshotId == null ? version : version.replace("SNAPSHOT", snapshotId)
        );
    }

    public String getFileName() {
        return String.format(FILE_NAME, artifactId, version);
    }

    public boolean validateChecksum(String checksum) {
        val decoder = Base64.getDecoder();

        return Arrays.equals(decoder.decode(checksum), decoder.decode(this.checksum));
    }

}