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
package org.guthix.buffer.codec

import org.guthix.buffer.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
internal class JagMessageEncoder(
    private val byteBuf: JByteBuf,
    override val serializersModule: SerializersModule
) : Encoder, CompositeEncoder {
    override fun encodeBoolean(value: Boolean) { byteBuf.writeBoolean(value) }
    override fun encodeChar(value: Char) { byteBuf.writeChar(value) }
    override fun encodeByte(value: Byte) { byteBuf.writeByte(value.toInt()) }
    override fun encodeShort(value: Short) { byteBuf.writeShort(value.toInt()) }
    override fun encodeInt(value: Int) { byteBuf.writeInt(value) }
    override fun encodeLong(value: Long) { byteBuf.writeLong(value) }
    override fun encodeString(value: String) { byteBuf.writeString(value) }
    @ExperimentalSerializationApi
    override fun encodeInline(inlineDescriptor: SerialDescriptor): Encoder = this
    @ExperimentalSerializationApi
    override fun encodeNull() { }
    override fun encodeFloat(value: Float) { TODO("Not yet implemented") }
    override fun encodeDouble(value: Double) { TODO("Not yet implemented") }
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) { TODO("Not yet implemented") }


    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JBoolean) byteBuf.writeBoolean(value)
        }
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JChar) byteBuf.writeChar(value)
        }
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JByte) annotation.type.writer(byteBuf, value.toInt())
        }
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JShort -> annotation.type.writer(byteBuf, value.toInt())
                is JShortSmart -> byteBuf.writeShortSmart(value.toInt())
            }
        }
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JMedium -> annotation.type.writer(byteBuf, value)
                is JInt -> annotation.type.writer(byteBuf, value)
                is JIntSmart -> byteBuf.writeIntSmart(value)
                is JVarInt -> byteBuf.writeVarInt(value)
            }
        }
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JSmallLong -> byteBuf.writeSmallLong(value)
                is JLong -> byteBuf.writeLong(value)
            }
        }
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JString -> byteBuf.writeString(value, annotation.charset.charset)
                is JVersionedString -> {
                    byteBuf.writeVersionedString(value, annotation.charset.charset, annotation.version)
                }
            }
        }
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        TODO("Not yet implemented")
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        TODO("Not yet implemented")
    }

    @ExperimentalSerializationApi
    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
        TODO("Not yet implemented")
    }

    @ExperimentalSerializationApi
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        TODO("Not yet implemented")
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val annotations = descriptor.getElementAnnotations(index)
        when (serializer) {
            UByte.serializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JByte -> annotation.type.writer(byteBuf, (value as UByte).toInt())
                }
            }
            UShort.serializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JShort -> annotation.type.writer(byteBuf, (value as UShort).toInt())
                    is JShortSmart -> byteBuf.writeUShortSmart((value as UShort).toInt())
                }
            }
            UInt.serializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JMedium -> annotation.type.writer(byteBuf, (value as UInt).toInt())
                    is JInt -> annotation.type.writer(byteBuf, (value as UInt).toInt())
                    is JIntSmart -> byteBuf.writeUIntSmart((value as UInt).toInt())
                }
            }
            ULong.serializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JSmallLong -> byteBuf.writeSmallLong((value as ULong).toLong())
                    is JLong -> byteBuf.writeLong((value as ULong).toLong())
                }
            }
            ByteArraySerializer() -> for (annotation in annotations) {
                when (annotation) {
                    is JByteArray -> annotation.type.writer(byteBuf, value as ByteArray)
                }
            }
        }
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = this

    override fun endStructure(descriptor: SerialDescriptor) { /* Nothing */ }
}