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

        compileOnly("org.mariadb.jdbc:mariadb-java-client:3.0.3")
        compileOnly("mysql:mysql-connector-java:8.0.28")
        compileOnly("com.h2database:h2:2.1.210")
        compileOnly("org.xerial:sqlite-jdbc:3.36.0.3")
        compileOnly("redis.clients:jedis:4.1.1")
        compileOnly("com.zaxxer:HikariCP:4.0.3")
        compileOnly("com.github.ben-manes.caffeine:caffeine:2.9.3")
        compileOnly("org.slf4j:slf4j-simple:1.7.36")
        compileOnly("org.slf4j:slf4j-api:1.7.36")
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
        archiveFileName.set("MiyukiEvents-$version.jar")

        val libsPackage = "app.miyuki.miyukievents.libs."

        relocate("org.intellij.lang.annotations", "${libsPackage}lang.annotations")
        relocate("org.jetbrains.annotations", "${libsPackage}jetbrains.annotations")

        relocate("com.zaxxer.hikari", "${libsPackage}hikari")
        relocate("org.slf4j", "${libsPackage}slf4j")

        relocate("com.google.errorprone.annotations", "${libsPackage}errorprone.annotations")
        relocate("com.google.gson", "${libsPackage}gson")
        relocate("com.mysql.cj", "${libsPackage}mysql")
        relocate("org.apache.commons.pool2", "${libsPackage}pool2")
        relocate("org.checkerframework", "${libsPackage}checkerframework")
        relocate("org.h2", "${libsPackage}h2")
        relocate("org.json", "${libsPackage}json")
        relocate("org.mariadb.jdbc", "${libsPackage}mariadb")
        relocate("org.sqlite", "${libsPackage}sqlite")
        relocate("redis.clients.jedis", "${libsPackage}jedis")
        relocate("com.google.protobuf", "${libsPackage}protobuf")

        relocate("com.github.benmanes.caffeine", "${libsPackage}caffeine")
    }
}