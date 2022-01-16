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
    implementation(project(":bungeecord"))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
//        options.compilerArgs.add("-parameters")
//        options.isFork = true
//        options.forkOptions.executable = "javac"
    }
    shadowJar {
        archiveFileName.set("MiyukiEvents-$version.jar")
        relocate("co.aikar.commands", "app.miyuki.libs.acf")
        relocate("co.aikar.locales", "app.miyuki.libs.locales")
        relocate("io.github.bananapuncher714.nbteditor", "app.miyuki.libs.nbteditor")
        relocate("org.intellij.lang.annotations", "app.miyuki.libs.intellij.lang.annotations")
        relocate("org.jetbrains.annotations", "app.miyuki.libs.jetbrains.annotations")
    }
}