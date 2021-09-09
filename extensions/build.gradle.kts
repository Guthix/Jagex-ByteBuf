import io.guthix.buffer.registerPublication

dependencies {
    api(deps.netty.buffer)
}

registerPublication(
    name = "jagex-bytebuf-extensions",
    description = "Netty ByteBuf extensions for RuneTek obfuscated buffers"
)