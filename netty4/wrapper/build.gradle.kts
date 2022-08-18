import io.guthix.buffer.registerPublication

dependencies {
    api(project(":netty4:extensions"))
    implementation(deps.netty4.codec)
}

registerPublication(name = "jagex-bytebuf-wrapper", description = "Netty ByteBuf wrapper")