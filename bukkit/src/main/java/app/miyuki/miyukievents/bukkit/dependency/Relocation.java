package app.miyuki.miyukievents.bukkit.dependency;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class Relocation {

    private final String oldPackage;
    private final String newPackage;

}
