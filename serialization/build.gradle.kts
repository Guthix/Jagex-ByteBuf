import io.guthix.buffer.registerPublication

plugins {
    kotlin("plugin.serialization") version "1.5.20"
}

dependencies {
    implementation(rootProject)
    implementation(libs.serialization.core)
    implementation(project(":wrapper"))
}

registerPublication(
    publicationName = "bytebufSerialization",
    pomName = "jagex-bytebuf-serialization"
)