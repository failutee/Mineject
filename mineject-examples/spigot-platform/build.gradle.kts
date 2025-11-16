import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "Mineject-Spigot-Platform"
version = "Example"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(dependencyNotation = "io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")

    implementation(project(":mineject-core"))
    implementation(project(":mineject-spigot"))
}

bukkit {
    main = "xyz.failutee.example.spigot.ExampleSpigotPlugin"

    name = "${project.group}"
    version = "${project.version}"

    apiVersion = "1.13"

    tasks.withType<ShadowJar> {
        mergeServiceFiles()
        minimize()
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks {
    runServer {
        minecraftVersion("1.20.4")
    }
}