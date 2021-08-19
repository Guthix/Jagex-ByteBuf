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

public enum class BByteMod { NONE, NEG, ADD, SUB }

@ExperimentalSerializationApi
public typealias JBoolean = JByte

@ExperimentalSerializationApi
public typealias JChar = JByte

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JByte(val mod: BByteMod = BByteMod.NONE)

public enum class SByteOrder { BE, LE }

public enum class SByteMod { NONE, ADD }

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JShort(val order: SByteOrder = SByteOrder.BE, val mod: SByteMod = SByteMod.NONE)

public enum class MByteOrder { BE, LME, RME }

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JMedium(val order: MByteOrder = MByteOrder.BE)

public enum class IByteOrder { BE, ME, IME, LE }

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JInt(val order: IByteOrder = IByteOrder.BE)

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

public enum class BaByteMod { NONE, ADD }

@SerialInfo
@ExperimentalSerializationApi
@Target(AnnotationTarget.PROPERTY)
public annotation class JByteArray(val mod: BaByteMod = BaByteMod.NONE, val reversed: Boolean = false)