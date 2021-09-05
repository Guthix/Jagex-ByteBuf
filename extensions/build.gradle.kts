import io.guthix.buffer.registerPublication

description = "Netty ByteBuf extensions for RuneTek obfuscated buffers"

dependencies {
    api(libs.netty.buf)
}

registerPublication("jagex-bytebuf-extensions")