import io.guthix.buffer.registerPublication

dependencies {
    api(deps.netty.buffer)
    implementation(deps.netty.codec)
    implementation(project(":extensions"))
}

registerPublication(name = "jagex-bytebuf-wrapper", description = "Netty ByteBuf wrapper")