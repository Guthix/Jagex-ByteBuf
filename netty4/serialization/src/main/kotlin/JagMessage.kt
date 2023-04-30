/*
 * Copyright 2018-2021 Guthix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.guthix.buffer

import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import org.guthix.buffer.codec.JagMessageDecoder
import org.guthix.buffer.codec.JagMessageEncoder

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
public sealed class JagMessage(
    internal val encodeDefaults: Boolean,
    private val serializersModule: SerializersModule
) {
    public companion object Default : JagMessage(false, EmptySerializersModule)

    private val sizeCache = mutableMapOf<SerializationStrategy<*>, Int>()

    public fun <T> encodeToByteBuf(
        serializer: SerializationStrategy<T>,
        value: T,
        size: Int? = null,
        allocator: ByteBufAllocator = ByteBufAllocator.DEFAULT
    ): JByteBuf {
        val descriptor = serializer.descriptor
        val actualSize = size ?: sizeCache.getOrPut(serializer) {
            (0 until descriptor.elementsCount).sumOf { index ->
                descriptor.getElementAnnotations(index).sumOf { it.byteSize() ?: 0 }
            }
        }
        val byteBuf = allocator.jBuffer(actualSize)
        val encoder = JagMessageEncoder(byteBuf, serializersModule)
        encoder.encodeSerializableValue(serializer, value)
        return byteBuf
    }

    private fun Annotation.byteSize(): Int? = when (this) {
        is JByte -> Byte.SIZE_BYTES
        is JShort -> Short.SIZE_BYTES
        is JShortSmart -> Short.SIZE_BYTES
        is JMedium -> Medium.SIZE_BYTES
        is JIntSmart -> Int.SIZE_BYTES
        is JInt -> Int.SIZE_BYTES
        is JSmallLong -> SmallLong.SIZE_BYTES
        is JLong -> Long.SIZE_BYTES
        else -> null
    }

    public fun <T> decodeFromByteBuf(deserializer: DeserializationStrategy<T>, byteBuf: JByteBuf): T {
        val decoder = JagMessageDecoder(serializersModule, byteBuf)
        return decoder.decodeSerializableValue(deserializer)
    }
}