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
package io.guthix.buffer.codec

import io.guthix.buffer.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
public class JagMessageDecoder(
    override val serializersModule: SerializersModule,
    private val byteBuf: JByteBuf
) : Decoder, CompositeDecoder {
    private var elementIter = 0

    override fun decodeBoolean(): Boolean = byteBuf.readBoolean()
    override fun decodeChar(): Char = byteBuf.readChar()
    override fun decodeByte(): Byte = byteBuf.readByte()
    override fun decodeShort(): Short = byteBuf.readShort()
    override fun decodeInt(): Int = byteBuf.readInt()
    override fun decodeLong(): Long = byteBuf.readLong()
    override fun decodeString(): String = byteBuf.readString()
    override fun decodeFloat(): Float { TODO("Not yet implemented") }
    override fun decodeDouble(): Double { TODO("Not yet implemented") }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int { TODO("Not yet implemented") }

    @ExperimentalSerializationApi
    override fun decodeInline(inlineDescriptor: SerialDescriptor): Decoder = this

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean { TODO("Not yet implemented") }

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? { TODO("Not yet implemented") }

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JBoolean) return byteBuf.readBoolean()
        }
        return byteBuf.readBoolean()
    }

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JChar) return byteBuf.readChar()
        }
        return byteBuf.readChar()
    }

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JByte) return annotation.type.sReader(byteBuf)
        }
        return byteBuf.readByte()
    }

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JShort -> return annotation.type.reader(byteBuf)
                is JShortSmart -> return byteBuf.readShortSmart()
            }
        }
        return byteBuf.readShort()
    }

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JMedium -> return annotation.type.sReader(byteBuf)
                is JInt -> return annotation.type.sReader(byteBuf)
                is JIntSmart -> return byteBuf.readIntSmart()
                is JVarInt -> return byteBuf.readVarInt()
            }
        }
        return byteBuf.readInt()
    }

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JSmallLong -> return byteBuf.readSmallLong()
                is JLong -> return byteBuf.readLong()
            }
        }
        return byteBuf.readLong()
    }

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JString -> return byteBuf.readString(annotation.charset.charset)
                is JVersionedString -> return byteBuf.readVersionedString(annotation.charset.charset, annotation.version)
            }
        }
        return byteBuf.readString()
    }

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float {
        TODO("Not yet implemented")
    }

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double {
        TODO("Not yet implemented")
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (!byteBuf.isReadable()) return CompositeDecoder.DECODE_DONE
        return elementIter++
    }

    @ExperimentalSerializationApi
    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder {
        TODO("Not yet implemented")
    }

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? {
        TODO("Not yet implemented")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        val annotations = descriptor.getElementAnnotations(index)
        when (deserializer) {
            UByte.serializer() -> for (annotation in annotations) {
                if (annotation is JByte) return annotation.type.uReader(byteBuf) as T
            }
            UShort.serializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JShort -> return annotation.type.uReader(byteBuf) as T
                    is JShortSmart -> return byteBuf.readUShortSmart() as T
                }
            }
            UInt.serializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JMedium -> return annotation.type.uReader(byteBuf) as T
                    is JInt -> return annotation.type.uReader(byteBuf) as T
                    is JIntSmart -> return byteBuf.readUIntSmart() as T
                }
            }
            ULong.serializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JSmallLong -> return byteBuf.readUSmallLong() as T
                    is JLong -> return byteBuf.readULong() as T
                }
            }
        }
        throw SerializationException("Could not decode for ${deserializer.descriptor.serialName}.")
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = this

    override fun endStructure(descriptor: SerialDescriptor) { /* Nothing */ }
}