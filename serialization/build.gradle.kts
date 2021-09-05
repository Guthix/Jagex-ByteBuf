import io.guthix.buffer.registerPublication

description = "Serialization for Jagex messages"

plugins {
    kotlin("plugin.serialization") version "1.5.20"
}

dependencies {
    implementation(rootProject)
    implementation(libs.serialization.core)
    implementation(project(":wrapper"))
    implementation(project(":extensions"))
}

registerPublication("jagex-bytebuf-serialization")