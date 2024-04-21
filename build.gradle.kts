import org.gradle.api.tasks.Delete

plugins {
    java
    `maven-publish`
}

allprojects {
    group = "xyz.failutee.mineject"
}

val cleanBuildOutput by tasks.registering(Delete::class) {
    delete(projectDir.resolve("build"))
}

tasks.named("build") {
    finalizedBy(cleanBuildOutput)
}

subprojects {
    plugins.apply("java")
    plugins.apply("maven-publish")

    tasks.withType<JavaCompile> {
        options.isIncremental = true
        options.encoding = "UTF-8"
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = project.name.toString()
                version = project.version.toString()
                from(components["java"])
            }
        }
    }
}
