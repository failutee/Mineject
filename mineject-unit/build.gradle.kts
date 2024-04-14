plugins {
    id("java")
}

group = "Mineject-Unit"

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