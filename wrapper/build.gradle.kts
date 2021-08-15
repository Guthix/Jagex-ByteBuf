@file:Suppress("UnstableApiUsage")

import io.guthix.buffer.registerPublication

description = "Netty ByteBuf wrapper"

dependencies {
    api(libs.netty.buf)
    implementation(libs.netty.codec)
    implementation(project(":extensions"))
}

registerPublication(
    publicationName = "bytebufWrapper",
    pomName = "jagex-bytebuf-wrapper"
)