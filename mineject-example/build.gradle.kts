import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "Mineject-Example"
version = "ExampleVersion"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    compileOnly(dependencyNotation = "org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

    implementation(project(":mineject-core"))
    implementation(project(":mineject-spigot"))
}

bukkit {
    main = "xyz.failutee.mineject.example.MinejectExample"

    name = "${project.group}"
    version = "${project.version}"
    author = "failutee"
    website = "https://github.com/failutee/Mineject"

    apiVersion = "1.13"
}

tasks {
    runServer {
        minecraftVersion("1.20.1")
    }
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    minimize()
}
