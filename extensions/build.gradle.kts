import io.guthix.buffer.registerPublication

dependencies {
    api(libs.netty.buffer)
}

registerPublication(
    name = "jagex-bytebuf-extensions",
    description = "Netty ByteBuf extensions for RuneTek obfuscated buffers"
)