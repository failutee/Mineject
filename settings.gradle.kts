plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "Mineject"
include("mineject-core")
include("mineject-spigot")
include("mineject-commons")
include("mineject-examples")
include("mineject-examples:spigot-platform")
