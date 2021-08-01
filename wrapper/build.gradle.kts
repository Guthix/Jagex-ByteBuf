@file:Suppress("UnstableApiUsage")

import io.guthix.buffer.registerPublication

dependencies {
    api(libs.netty.buf)
}


registerPublication(
    publicationName = "bytebufWrapper",
    pomName = "jagex-bytebuf-wrapper"
)