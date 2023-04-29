import org.guthix.buffer.registerPublication

plugins {
    kotlin("plugin.serialization") version "1.5.31"
}

dependencies {
    implementation(deps.serialization.core)
    implementation(project(":netty4:wrapper"))
    implementation(project(":netty4:extensions"))
}

registerPublication(name = "jagex-bytebuf-serialization", description = "Serialization for Jagex messages")