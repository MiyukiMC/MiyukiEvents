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

// nChat, LegendChat, yClans, SimpleClans, WorldEdit, bStats (SOekd), utf8
dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.10.9")

    implementation("io.github.bananapuncher714:nbteditor:7.18.0")

    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    compileOnly("com.github.cryptomorin:XSeries:8.5.0.1")

    compileOnly("com.zaxxer:HikariCP:4.0.3")
}