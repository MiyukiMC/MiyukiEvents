plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "app.miyuki"
version = "1.0-SNAPSHOT"


allprojects {

    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.22")
        annotationProcessor("org.projectlombok:lombok:1.18.22")
    }

}

dependencies {
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
//    shadowJar {
//        archiveFileName.set("MiyukiEvents-$version.jar")
//    }
}