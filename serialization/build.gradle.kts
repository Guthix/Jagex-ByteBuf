import io.guthix.buffer.registerPublication

plugins {
    kotlin("plugin.serialization") version "1.5.20"
}

dependencies {
    implementation(rootProject)
    implementation(deps.serialization.core)
    implementation(project(":wrapper"))
    implementation(project(":extensions"))
}

registerPublication(name = "jagex-bytebuf-serialization", description = "Serialization for Jagex messages")