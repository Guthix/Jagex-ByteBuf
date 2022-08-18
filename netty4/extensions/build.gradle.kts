import io.guthix.buffer.registerPublication

dependencies {
    api(deps.netty4.buffer)
}

registerPublication(
    name = "jagex-bytebuf-extensions",
    description = "Netty ByteBuf extensions for RuneTek obfuscated buffers"
)