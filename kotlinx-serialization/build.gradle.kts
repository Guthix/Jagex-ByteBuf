import io.guthix.buffer.registerPublication

dependencies {
    implementation(rootProject)
    implementation(libs.serialization.core)
}

registerPublication(
    publicationName = "kotlinxSerialization",
    pomName = "kotlinx-serialization-jagprot"
)