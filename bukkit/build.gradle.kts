repositories {
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.10.9")

    implementation("io.github.bananapuncher714:nbteditor:7.18.1")

    implementation("com.github.cryptomorin:XSeries:8.6.1")

    implementation("org.bstats:bstats-bukkit:3.0.0")

    compileOnly("net.kyori:adventure-platform-bukkit:4.1.1")

    compileOnly("net.sacredlabyrinth.phaed.simpleclans:SimpleClans:2.15.0")
}

tasks {
    shadowJar {
        relocate("org.bstats", "app.miyuki.miyukievents.libs.bstats")
        relocate("com.cryptomorin.xseries", "app.miyuki.miyukievents.libs.xseries")
        relocate("io.github.bananapuncher714.nbteditor", "app.miyuki.libs.miyukievents.nbteditor")
    }
}
