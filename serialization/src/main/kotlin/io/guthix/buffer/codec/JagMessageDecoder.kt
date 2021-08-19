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
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
public class JagMessageDecoder(
    override val serializersModule: SerializersModule,
    private val byteBuf: JByteBuf
) : Decoder, CompositeDecoder {
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
    override fun decodeInline(inlineDescriptor: SerialDescriptor): Decoder { TODO("Not yet implemented") }

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean { TODO("Not yet implemented") }

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? { TODO("Not yet implemented") }

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JBoolean) {
                return when(annotation.mod) {
                    BByteMod.NONE -> byteBuf.readBoolean()
                    BByteMod.NEG -> byteBuf.readByteNeg().toInt() == 1
                    BByteMod.ADD -> byteBuf.readByteAdd().toInt() == 1
                    BByteMod.SUB -> byteBuf.readByteSub().toInt() == 1
                }
            }
        }
        return byteBuf.readBoolean()
    }

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JChar) {
                return when(annotation.mod) {
                    BByteMod.NONE -> byteBuf.readChar()
                    BByteMod.NEG -> byteBuf.readByteNeg().toInt().toChar()
                    BByteMod.ADD -> byteBuf.readByteAdd().toInt().toChar()
                    BByteMod.SUB -> byteBuf.readByteSub().toInt().toChar()
                }
            }
        }
        return byteBuf.readChar()
    }

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            if (annotation is JByte) {
                return when(annotation.mod) {
                    BByteMod.NONE -> byteBuf.readByte()
                    BByteMod.NEG -> byteBuf.readByteNeg()
                    BByteMod.ADD -> byteBuf.readByteAdd()
                    BByteMod.SUB -> byteBuf.readByteSub()
                }
            }
        }
        return byteBuf.readByte()
    }

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JShort -> return when (annotation.order) {
                    SByteOrder.BE -> when (annotation.mod) {
                        SByteMod.NONE -> byteBuf.readShort()
                        SByteMod.ADD -> byteBuf.readShortAdd()
                    }
                    SByteOrder.LE -> when (annotation.mod) {
                        SByteMod.NONE -> byteBuf.readShortLE()
                        SByteMod.ADD -> byteBuf.readShortLEAdd()
                    }
                }
                is JShortSmart -> return byteBuf.readShortSmart()
            }
        }
        return byteBuf.readShort()
    }

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
        val annotations = descriptor.getElementAnnotations(index)
        for (annotation in annotations) {
            when (annotation) {
                is JInt -> return when(annotation.order) {
                    IByteOrder.BE -> byteBuf.readInt()
                    IByteOrder.ME -> byteBuf.readIntME()
                    IByteOrder.IME -> byteBuf.readIntIME()
                    IByteOrder.LE -> byteBuf.readIntLE()
                }
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
                is JString -> byteBuf.readString(annotation.charset.charset)
                is JVersionedString -> byteBuf.readVersionedString(annotation.charset.charset, annotation.version)
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
        TODO("Not yet implemented")
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

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        TODO("Not yet implemented")
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        TODO("Not yet implemented")
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        TODO("Not yet implemented")
    }

}