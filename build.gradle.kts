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
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")

        implementation("org.jetbrains:annotations:23.0.0")

        implementation("net.kyori:adventure-text-minimessage:4.11.0")

        implementation("org.mariadb.jdbc:mariadb-java-client:3.0.6")
        implementation("mysql:mysql-connector-java:8.0.30")
        implementation("com.h2database:h2:2.1.214")
        implementation("org.xerial:sqlite-jdbc:3.36.0.3")
        implementation("redis.clients:jedis:4.2.3")
        implementation("com.zaxxer:HikariCP:4.0.3")
        implementation("org.slf4j:slf4j-simple:1.7.36")
        implementation("org.slf4j:slf4j-api:1.7.36")

        implementation("org.spongepowered:configurate-yaml:4.1.2")
    }

}

dependencies {
    implementation(project(":bukkit", "shadow"))
    implementation(project(":worldedit-6"))
    implementation(project(":worldedit-7"))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    shadowJar {
        archiveFileName.set("MiyukiEvents-${project.version}.jar")

        val libsPackage = "app.miyuki.miyukievents.libs."

        relocate("org.intellij.lang.annotations", "${libsPackage}lang.annotations")
        relocate("org.jetbrains.annotations", "${libsPackage}jetbrains.annotations")
        relocate("com.zaxxer.hikari", "${libsPackage}hikari")
        relocate("org.slf4j", "${libsPackage}slf4j")
        relocate("com.google.errorprone.annotations", "${libsPackage}errorprone.annotations")
        relocate("com.mysql.cj", "${libsPackage}mysql")
        relocate("org.apache.commons.pool2", "${libsPackage}pool2")
        relocate("org.h2", "${libsPackage}h2")
        relocate("org.json", "${libsPackage}json")
        relocate("org.mariadb.jdbc", "${libsPackage}mariadb")
        relocate("redis.clients.jedis", "${libsPackage}jedis")
        relocate("com.google.protobuf", "${libsPackage}protobuf")
        relocate("net.kyori.examination", "${libsPackage}kyori.examination")
        relocate("net.kyori.adventure", "${libsPackage}kyori.adventure")

    }
}