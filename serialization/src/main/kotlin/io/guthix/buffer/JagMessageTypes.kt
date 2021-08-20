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
    public val reader: JByteBuf.() -> Byte,
    public val writer: JByteBuf.(Int) -> JByteBuf
) {
    DEFAULT(JByteBuf::readByte, JByteBuf::writeByte),
    NEG(JByteBuf::readByteNeg, JByteBuf::writeByteNeg),
    ADD(JByteBuf::readByteAdd, JByteBuf::writeByteAdd),
    SUB(JByteBuf::readByteSub, JByteBuf::writeByteSub)
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
    public val writer: JByteBuf.(Int) -> JByteBuf
) {
    DEFAULT(JByteBuf::readShort, JByteBuf::writeShort),
    LE(JByteBuf::readShortLE, JByteBuf::writeShortLE),
    ADD(JByteBuf::readShortAdd, JByteBuf::writeShortAdd),
    LE_ADD(JByteBuf::readShortLEAdd, JByteBuf::writeShortAdd)
}

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JShort(val type: JShortType = JShortType.DEFAULT)

public enum class JMediumType { DEFAULT, LME, RME }

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JMedium(val type: JMediumType = JMediumType.DEFAULT)

public enum class JIntType(
    public val reader: JByteBuf.() -> Int,
    public val writer: JByteBuf.(Int) -> JByteBuf
) {
    DEFAULT(JByteBuf::readInt, JByteBuf::writeInt),
    ME(JByteBuf::readIntME, JByteBuf::writeIntME),
    IME(JByteBuf::readIntIME, JByteBuf::writeIntIME),
    LE(JByteBuf::readIntLE, JByteBuf::writeIntLE)
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

public enum class JCharSet(public val charset: Charset) { WINDOWS_1252(windows1252), CESU8(cesu8) }

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JString(val charset: JCharSet = JCharSet.WINDOWS_1252)

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JVersionedString(val charset: JCharSet = JCharSet.WINDOWS_1252, val version: Int = 0)

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