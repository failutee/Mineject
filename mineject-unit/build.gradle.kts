plugins {
    id("java")
    id("maven-publish")
}

version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":mineject-core"))
}

tasks.test {
    useJUnitPlatform()
}