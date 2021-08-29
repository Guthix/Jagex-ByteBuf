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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo
import java.nio.charset.Charset

public enum class JByteType(
    public val sReader: JByteBuf.() -> Byte,
    public val uReader: JByteBuf.() -> UByte,
    public val writer: JByteBuf.(Int) -> JByteBuf
) {
    DEFAULT(JByteBuf::readByte, JByteBuf::readUByte, JByteBuf::writeByte),
    NEG(JByteBuf::readByteNeg, JByteBuf::readUByteNeg, JByteBuf::writeByteNeg),
    ADD(JByteBuf::readByteAdd, JByteBuf::readUByteAdd, JByteBuf::writeByteAdd),
    SUB(JByteBuf::readByteSub, JByteBuf::readUByteSub, JByteBuf::writeByteSub)
}

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JBoolean

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JChar

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JByte(val type: JByteType = JByteType.DEFAULT)

public enum class JShortType(
    public val reader: JByteBuf.() -> Short,
    public val uReader: JByteBuf.() -> UShort,
    public val writer: JByteBuf.(Int) -> JByteBuf
) {
    DEFAULT(JByteBuf::readShort, JByteBuf::readUShort, JByteBuf::writeShort),
    LE(JByteBuf::readShortLE, JByteBuf::readUShortLE, JByteBuf::writeShortLE),
    ADD(JByteBuf::readShortAdd, JByteBuf::readUShortAdd, JByteBuf::writeShortAdd),
    LE_ADD(JByteBuf::readShortLEAdd, JByteBuf::readUShortLEAdd, JByteBuf::writeShortLEAdd)
}

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JShort(val type: JShortType = JShortType.DEFAULT)

public enum class JMediumType(
    public val sReader: JByteBuf.() -> Int,
    public val uReader: JByteBuf.() -> UInt,
    public val writer: JByteBuf.(Int) -> JByteBuf
) {
    DEFAULT(JByteBuf::readMedium, JByteBuf::readUMedium, JByteBuf::writeMedium),
    LME(JByteBuf::readMediumLME, JByteBuf::readUMediumLME, JByteBuf::writeMediumLME),
    RME(JByteBuf::readMediumRME, JByteBuf::readUMediumRME, JByteBuf::writeMediumRME)
}

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JMedium(val type: JMediumType = JMediumType.DEFAULT)

public enum class JIntType(
    public val sReader: JByteBuf.() -> Int,
    public val uReader: JByteBuf.() -> UInt,
    public val writer: JByteBuf.(Int) -> JByteBuf
) {
    DEFAULT(JByteBuf::readInt, JByteBuf::readUInt, JByteBuf::writeInt),
    ME(JByteBuf::readIntME, JByteBuf::readUIntME, JByteBuf::writeIntME),
    IME(JByteBuf::readIntIME, JByteBuf::readUIntIME, JByteBuf::writeIntIME),
    LE(JByteBuf::readIntLE, JByteBuf::readUIntLE, JByteBuf::writeIntLE)
}

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JInt(val type: JIntType = JIntType.DEFAULT)

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JSmallLong

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JLong

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JShortSmart

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JIntSmart

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JIncrShortSmart

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JVarInt

public enum class JCharSet(public val charset: Charset) { CP_1252(Charsets.CP_1252), CESU8(Charsets.CESU_8) }

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JString(val charset: JCharSet = JCharSet.CP_1252)

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JVersionedString(val charset: JCharSet = JCharSet.CP_1252, val version: Int = 0)

public enum class JByteArrayType(
    public val reader: JByteBuf.(ByteArray) -> JByteBuf,
    public val writer: JByteBuf.(JByteBuf) -> JByteBuf
) {
    DEFAULT(JByteBuf::readBytes, JByteBuf::writeBytes),
    ADD(JByteBuf::readBytesAdd, JByteBuf::writeBytesAdd),
    REVERSED(JByteBuf::readBytesReversed, JByteBuf::writeBytesReversed),
    REVERSED_ADD(JByteBuf::readBytesReversedAdd, JByteBuf::writeBytesReversedAdd)
}

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JByteArray(val type: JByteArrayType = JByteArrayType.DEFAULT)