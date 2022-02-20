package app.miyuki.miyukievents.bukkit.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Dependency {

  ASM(
          "org.ow2.asm",
          "asm",
          "9.2"
  );



    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String repository;
    private final Relocation relocation;

    Dependency(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, "https://repo1.maven.org/maven2/", null);
    }

    Dependency(String groupId, String artifactId, String version, Relocation relocation) {
        this(groupId, artifactId, version, "https://repo1.maven.org/maven2/", relocation);
    }


}
