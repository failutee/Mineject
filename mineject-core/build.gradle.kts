import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "Mineject-Core"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.google.guava:guava:33.1.0-jre")
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    minimize()
}

artifacts {
    add("archives", tasks.getByName<ShadowJar>("shadowJar"))
}

tasks.test {
    useJUnitPlatform()
}