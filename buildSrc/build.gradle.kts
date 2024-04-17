plugins {
    `kotlin-dsl`
    id("maven-publish")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

allprojects {
    version = "1.0.0"
    group = "xyz.failutee.mineject"
}

subprojects {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "${project.name}-${project.version.toString()}"

                groupId = project.group.toString()

                from(components["java"])
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
}