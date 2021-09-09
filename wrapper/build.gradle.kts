import io.guthix.buffer.registerPublication

dependencies {
    api(libs.netty.buf)
    implementation(libs.netty.codec)
    implementation(project(":extensions"))
}

registerPublication( name = "jagex-bytebuf-wrapper", description = "Netty ByteBuf wrapper")