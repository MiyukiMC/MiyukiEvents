plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "app.miyuki"
version = "1.0.0-SNAPSHOT"


allprojects {

    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://repo.codemc.org/repository/maven-public/")
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.22")
        annotationProcessor("org.projectlombok:lombok:1.18.22")

        implementation("org.jetbrains:annotations:22.0.0")
    }

}

dependencies {
    implementation(project(":bukkit"))
    implementation(project(":worldedit-6"))
    implementation(project(":worldedit-7"))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    shadowJar {
        archiveFileName.set("MiyukiEvents-$version.jar")
        relocate("io.github.bananapuncher714.nbteditor", "app.miyuki.libs.miyukievents.nbteditor")
        relocate("org.intellij.lang.annotations", "app.miyuki.libs.miyukievents.intellij.lang.annotations")
        relocate("org.jetbrains.annotations", "app.miyuki.miyukievents.libs.jetbrains.annotations")
        relocate("org.bstats", "app.miyuki.miyukievents.libs.bstats")
    }
}