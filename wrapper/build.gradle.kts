@file:Suppress("UnstableApiUsage")

import io.guthix.buffer.registerPublication

description = "Netty ByteBuf wrapper"

dependencies {
    api(libs.netty.buf)
}

registerPublication(
    publicationName = "bytebufWrapper",
    pomName = "jagex-bytebuf-wrapper"
)