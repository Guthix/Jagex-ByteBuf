import io.guthix.buffer.registerPublication

plugins {
    kotlin("plugin.serialization") version "1.5.0"
}

dependencies {
    implementation(rootProject)
    implementation(libs.serialization.core)
}

registerPublication(
    publicationName = "bytebufSerialization",
    pomName = "jagex-bytebuf-serialization"
)