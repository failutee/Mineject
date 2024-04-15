plugins {
    kotlin("jvm") version "1.9.23"
    id("maven-publish")
}

repositories {
    mavenCentral()
}

allprojects {
    version = "1.0.0"
    group = "xyz.failutee.mineject"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name

            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
        }
    }
}

kotlin {
    jvmToolchain(17)
}