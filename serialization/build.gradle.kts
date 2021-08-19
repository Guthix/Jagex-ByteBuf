import io.guthix.buffer.registerPublication

plugins {
    kotlin("plugin.serialization") version "1.5.20"
}

dependencies {
    implementation(rootProject)
    implementation(libs.serialization.core)
    implementation(project(":wrapper"))
    implementation(project(":extensions"))
}

registerPublication(
    publicationName = "bytebufSerialization",
    pomName = "jagex-bytebuf-serialization"
)