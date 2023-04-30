# Jagex-ByteBuf
[![Release](https://img.shields.io/maven-central/v/org.guthix/jagex-bytebuf-extensions)](https://search.maven.org/search?q=jagex-bytebuf)
[![Snapshot](https://img.shields.io/nexus/s/org.guthix/jagex-bytebuf-extensions?server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/content/repositories/snapshots/org/guthix/)
[![License](https://img.shields.io/github/license/guthix/Jagex-ByteBuf)](https://github.com/guthix/Jagex-ByteBuf/blob/master/LICENSE)
[![Discord](https://img.shields.io/discord/538667877180637184?color=%237289da&logo=discord)](https://discord.gg/AFyGxNp)

Jagex-ByteBuf is a set of Netty ByteBuf utilities for RuneTek game emulation. Jagex-ByteBuf contains 3 different modules;
extensions, wrapper and serialization which are discussed in detail below.

### Extensions
Contains a set of `ByteBuf` extension methods to encode and decode RuneTek specific types and protocol obfuscations, for
example: 
```Kotlin 
public fun ByteBuf.readShortLEAdd(index: Int) { ... }
```
reads 16-bits in little endian byte-order and negates 128 from the resulting value.

### Wrapper
The wrapper module provides a wrapper, called `JByteBuf` around `ByteBuf` that, instead of upcasting the unsigned value to next biggest primitive returns an unsigned type. Using the wrapper is recommended because it results in stronger typing. Most methods that are available in `ByteBuf` are also available in `JByteBuf`. Creating a `JByteBuf` can be done by calling `ByteBufAllocator#jBuffer`.

### Serialization
Provides a [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) implementation for writing messages used in the RuneTek protocol. Jagex-ByteBuf-Serialization is similar to [Protobuf](https://github.com/protocolbuffers/protobuf) but instead of specifying the encoding through a serializable format it uses annotations. Example:

```Kotlin
@Serialization
class VarpLargePacket(
    @JShort(JShortType.LE_ADD) private val id: Short,
    @JInt(JIntType.IME) private val state: Int
)
```

### Usage
Artifacts:
```Kotlin
dependencies {
    implementation(group = "org.guthix", name = "jagex-bytebuf-extensions", version = VERSION)
    implementation(group = "org.guthix", name = "jagex-bytebuf-wrapper", version = VERSION)
    implementation(group = "org.guthix", name = "jagex-bytebuf-serialization", version = VERSION)
}
```
Snapshot repository:
```Kotlin
repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}
```
When using the Serialization module:
```Kotlin
plugins {
    kotlin("plugin.serialization") version KOTLINX_SERIALIZATION_VERSION
}
```
