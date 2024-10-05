plugins {
    id("java")
    `java-library`
}

group = "xyz.failutee.mineject"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly(dependencyNotation = "org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

    implementation(project(":mineject-core"))
    api(project(":mineject-commons"))
}
