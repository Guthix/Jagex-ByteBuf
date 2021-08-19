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
package io.guthix.buffer

import io.guthix.buffer.codec.JagMessageDecoder
import io.guthix.buffer.codec.JagMessageEncoder
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
public sealed class JagMessage(
    internal val encodeDefaults: Boolean,
    private val serializersModule: SerializersModule
)  {
    public companion object Default : JagMessage(false, EmptySerializersModule)

    private val sizeCache = mutableMapOf<String, Int>()

    public fun <T> encodeToByteBuf(
        serializer: SerializationStrategy<T>,
        value: T
    ): JByteBuf {
        val descriptor = serializer.descriptor
        val byteBuf = ByteBufAllocator.DEFAULT.jBuffer(
            sizeCache.getOrPut(descriptor.serialName) { calculateSize(descriptor) }
        )
        val encoder = JagMessageEncoder(byteBuf, serializersModule)
        encoder.encodeSerializableValue(serializer, value)
        return byteBuf
    }

    private fun calculateSize(descriptor: SerialDescriptor): Int {
        if (descriptor.elementsCount == 0) {
            for (annotation in descriptor.annotations) {
                when (annotation) {
                    is JByte -> return Byte.SIZE_BYTES
                    is JShort -> return Short.SIZE_BYTES
                    is JShortSmart -> return Short.SIZE_BYTES
                    is JMedium -> return Medium.SIZE_BYTES
                    is JIntSmart -> return Int.SIZE_BYTES
                    is JInt -> return Int.SIZE_BYTES
                    is JSmallLong -> return SmallLong.SIZE_BYTES
                    is JLong -> return Long.SIZE_BYTES
                }
            }
        }
        return descriptor.elementDescriptors.sumOf(::calculateSize)
    }

    public fun <T> decodeFromByteBuf(deserializer: DeserializationStrategy<T>, byteBuf: JByteBuf): T {
        val decoder = JagMessageDecoder(serializersModule, byteBuf)
        return decoder.decodeSerializableValue(deserializer)
    }
}