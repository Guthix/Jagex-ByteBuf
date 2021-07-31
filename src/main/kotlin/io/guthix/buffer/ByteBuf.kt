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

import io.netty.buffer.ByteBuf
import io.netty.util.ByteProcessor
import java.io.IOException
import java.nio.charset.Charset
import kotlin.Long

private const val HALF_BYTE = 128

/** Cp1252 */
public val windows1252: Charset = Charset.availableCharsets()["windows-1252"] ?: throw IllegalStateException(
    "Could not find CP1252 character set."
)

public val cesu8: Charset = Charset.availableCharsets()["CESU-8"] ?: throw IllegalStateException(
    "Could not find CESU-8 character set."
)

public fun ByteBuf.getByteNeg(index: Int): Byte = (-getByte(index)).toByte()

public fun ByteBuf.getByteAdd(index: Int): Byte = (getByte(index) - HALF_BYTE).toByte()

public fun ByteBuf.getByteSub(index: Int): Byte = (HALF_BYTE - getByte(index)).toByte()

public fun ByteBuf.getUnsignedByteNeg(index: Int): Short = (-getByte(index) and 0xFF).toShort()

public fun ByteBuf.getUnsignedByteAdd(index: Int): Short = ((getByte(index) - HALF_BYTE) and 0xFF).toShort()

public fun ByteBuf.getUnsignedByteSub(index: Int): Short = ((HALF_BYTE - getByte(index)) and 0xFf).toShort()

public fun ByteBuf.getShortAdd(index: Int): Short = ((getByte(index).toInt() shl Byte.SIZE_BITS) or
    ((getByte(index + 1) - HALF_BYTE) and 0xFF)).toShort()

public fun ByteBuf.getShortLEAdd(index: Int): Short = (((getByte(index) - HALF_BYTE) and 0xFF) or
    (getByte(index + 1).toInt() shl Byte.SIZE_BITS)).toShort()

public fun ByteBuf.getUnsignedShortAdd(index: Int): Int = (getUnsignedByte(index).toInt() shl Byte.SIZE_BITS) or
    ((getByte(index + 1) - HALF_BYTE) and 0xFF)

public fun ByteBuf.getUnsignedShortLEAdd(index: Int): Int = ((getByte(index) - HALF_BYTE) and 0xFF) or
    (getUnsignedByte(index + 1).toInt() shl Byte.SIZE_BITS)

public fun ByteBuf.getMediumLME(index: Int): Int = (getShortLE(index).toInt() shl Byte.SIZE_BITS) or
    getUnsignedByte(index + Short.SIZE_BYTES).toInt()

public fun ByteBuf.getMediumRME(index: Int): Int = (getByte(index).toInt() shl Short.SIZE_BITS) or
    getUnsignedShortLE(index + Byte.SIZE_BYTES)

public fun ByteBuf.getUnsignedMediumLME(index: Int): Int = (getUnsignedShortLE(index) shl Byte.SIZE_BITS) or
    getUnsignedByte(index + Short.SIZE_BYTES).toInt()

public fun ByteBuf.getUnsignedMediumRME(index: Int): Int = (getUnsignedByte(index).toInt() shl Short.SIZE_BITS) or
    getUnsignedShortLE(index + Byte.SIZE_BYTES)

public fun ByteBuf.getIntME(index: Int): Int = (getShortLE(index).toInt() shl Short.SIZE_BITS) or
    getUnsignedShortLE(index + Short.SIZE_BYTES)

public fun ByteBuf.getIntIME(index: Int): Int =
    getUnsignedShort(index) or (getShort(index + Short.SIZE_BYTES).toInt() shl Short.SIZE_BITS)

public fun ByteBuf.getUnsignedIntME(index: Int): Long = (getUnsignedShortLE(index).toLong() shl Short.SIZE_BITS) or
    getUnsignedShortLE(index + Short.SIZE_BYTES).toLong()

public fun ByteBuf.getUnsignedIntIME(index: Int): Long = getUnsignedShort(index).toLong() or
    (getUnsignedShort(index + Short.SIZE_BYTES).toLong() shl Short.SIZE_BITS)

public fun ByteBuf.getSmallLong(index: Int): Long = (getMedium(index).toLong() shl Medium.SIZE_BITS) or
    getUnsignedMedium(index + Medium.SIZE_BYTES).toLong()

public fun ByteBuf.getUnsignedSmallLong(index: Int): Long = (getUnsignedMedium(index).toLong() shl Medium.SIZE_BITS) or
    getUnsignedMedium(index + Medium.SIZE_BYTES).toLong()

public fun ByteBuf.getBytesAdd(index: Int, length: Int): ByteArray =
    getBytesAdd(index, ByteArray(length))

public fun ByteBuf.getBytesAdd(index: Int, dest: ByteArray): ByteArray = dest.apply {
    getBytes(index, this)
}.map { (it - HALF_BYTE).toByte() }.toByteArray()

public fun ByteBuf.getBytesReversedAdd(index: Int, length: Int): ByteArray =
    getBytesReversedAdd(index, ByteArray(length))

public fun ByteBuf.getBytesReversedAdd(index: Int, dest: ByteArray): ByteArray = dest.apply {
    getBytes(index, this)
}.map { (it - HALF_BYTE).toByte() }.reversed().toByteArray()

public fun ByteBuf.getBytesReversed(index: Int, length: Int): ByteArray =
    getBytesReversed(index, ByteArray(length))

public fun ByteBuf.getBytesReversed(index: Int, dest: ByteArray): ByteArray = dest.apply {
    getBytes(index, this)
}.reversed().toByteArray()

public fun ByteBuf.setByteNeg(index: Int, value: Int): ByteBuf = setByte(index, -value)

public fun ByteBuf.setByteAdd(index: Int, value: Int): ByteBuf = setByte(index, value + HALF_BYTE)

public fun ByteBuf.setByteSub(index: Int, value: Int): ByteBuf = setByte(index, HALF_BYTE - value)

public fun ByteBuf.setShortAdd(index: Int, value: Int): ByteBuf {
    setByte(index, value shr Byte.SIZE_BITS)
    setByte(index + Byte.SIZE_BYTES, value + HALF_BYTE)
    return this
}

public fun ByteBuf.setShortLEAdd(index: Int, value: Int): ByteBuf {
    setByte(index, value + HALF_BYTE)
    setByte(index + Byte.SIZE_BYTES, value shr Byte.SIZE_BITS)
    return this
}

public fun ByteBuf.setMediumLME(index: Int, value: Int): ByteBuf {
    setShortLE(index, value shr Byte.SIZE_BITS)
    setByte(index + Short.SIZE_BYTES, value)
    return this
}

public fun ByteBuf.setMediumRME(index: Int, value: Int): ByteBuf {
    setByte(index, value shr Short.SIZE_BITS)
    setShortLE(index + Byte.SIZE_BYTES, value)
    return this
}

public fun ByteBuf.setIntME(index: Int, value: Int): ByteBuf {
    setShortLE(index, value shr Short.SIZE_BITS)
    setShortLE(index + Short.SIZE_BYTES, value)
    return this
}

public fun ByteBuf.setIntIME(index: Int, value: Int): ByteBuf {
    setShort(index, value)
    setShort(index + Short.SIZE_BYTES, value shr Short.SIZE_BITS)
    return this
}

public fun ByteBuf.setSmallLong(index: Int, value: Long): ByteBuf {
    setMedium(index, (value shr Medium.SIZE_BITS).toInt())
    setMedium(index + Medium.SIZE_BYTES, value.toInt())
    return this
}

public fun ByteBuf.setBytesAdd(index: Int, src: ByteArray): ByteBuf = setBytes(index, src.map {
    (it + HALF_BYTE).toByte()
}.toByteArray())

public fun ByteBuf.setBytesAdd(index: Int, src: ByteBuf): ByteBuf {
    var j = index
    for (i in src.readerIndex() until src.writerIndex()) {
        setByte(j, src.getByte(i) + HALF_BYTE)
        j++
    }
    return this
}

public fun ByteBuf.setBytesReversedAdd(index: Int, src: ByteArray): ByteBuf = setBytes(index, src.map {
    (it + HALF_BYTE).toByte()
}.reversed().toByteArray())

public fun ByteBuf.setBytesReversedAdd(index: Int, src: ByteBuf): ByteBuf {
    var j = index
    for (i in src.writerIndex() - 1 downTo src.readerIndex()) {
        setByte(j, src.getByte(i) + HALF_BYTE)
        j++
    }
    return this
}

public fun ByteBuf.setBytesReversed(index: Int, src: ByteArray): ByteBuf = setBytes(
    index, src.reversed().toByteArray()
)

public fun ByteBuf.setBytesReversed(index: Int, src: ByteBuf): ByteBuf {
    var j = index
    for (i in src.writerIndex() - 1 downTo src.readerIndex()) {
        setByte(j, src.getByte(i).toInt())
        j++
    }
    return this
}

public fun ByteBuf.readByteNeg(): Byte = (-readByte()).toByte()

public fun ByteBuf.readByteAdd(): Byte = (readByte() - HALF_BYTE).toByte()

public fun ByteBuf.readByteSub(): Byte = (HALF_BYTE - readByte()).toByte()

public fun ByteBuf.readUnsignedByteNeg(): Short = (-readByte() and 0xFF).toShort()

public fun ByteBuf.readUnsignedByteAdd(): Short = ((readByte() - HALF_BYTE) and 0xFF).toShort()

public fun ByteBuf.readUnsignedByteSub(): Short = ((HALF_BYTE - readByte()) and 0xFF).toShort()

public fun ByteBuf.readShortAdd(): Short = ((readByte().toInt() shl Byte.SIZE_BITS) or
    ((readByte() - HALF_BYTE) and 0xFF)).toShort()

public fun ByteBuf.readShortLEAdd(): Short = (((readByte() - HALF_BYTE) and 0xFF) or
    (readByte().toInt() shl Byte.SIZE_BITS)).toShort()

public fun ByteBuf.readUnsignedShortAdd(): Int = (readUnsignedByte().toInt() shl Byte.SIZE_BITS) or
    ((readByte() - HALF_BYTE) and 0xFF)

public fun ByteBuf.readUnsignedShortLEAdd(): Int = ((readByte() - HALF_BYTE) and 0xFF) or
    (readUnsignedByte().toInt() shl Byte.SIZE_BITS)

public fun ByteBuf.readMediumLME(): Int = (readShortLE().toInt() shl Byte.SIZE_BITS) or
    readUnsignedByte().toInt()

public fun ByteBuf.readMediumRME(): Int = (readByte().toInt() shl Short.SIZE_BITS) or readUnsignedShortLE()

public fun ByteBuf.readUnsignedMediumLME(): Int = (readUnsignedShortLE() shl Byte.SIZE_BITS) or
    readUnsignedByte().toInt()

public fun ByteBuf.readUnsignedMediumRME(): Int = (readUnsignedByte().toInt() shl Short.SIZE_BITS) or
    readUnsignedShortLE()

public fun ByteBuf.readIntME(): Int = (readShortLE().toInt() shl Short.SIZE_BITS) or readUnsignedShortLE()

public fun ByteBuf.readIntIME(): Int = readUnsignedShort() or (readShort().toInt() shl Short.SIZE_BITS)

public fun ByteBuf.readUnsignedIntME(): Long = (readUnsignedShortLE().toLong() shl Short.SIZE_BITS) or
    readUnsignedShortLE().toLong()

public fun ByteBuf.readUnsignedIntIME(): Long = readUnsignedShort().toLong() or
    (readUnsignedShort().toLong() shl Short.SIZE_BITS)

public fun ByteBuf.readSmallLong(): Long = (readMedium().toLong() shl Medium.SIZE_BITS) or readUnsignedMedium().toLong()

public fun ByteBuf.readUnsignedSmallLong(): Long = (readUnsignedMedium().toLong() shl Medium.SIZE_BITS) or
    readUnsignedMedium().toLong()

public fun ByteBuf.readShortSmart(): Short {
    val peek = getByte(readerIndex()).toInt()
    return if (peek >= 0) {
        (readUnsignedByte().toInt() - Smart.BYTE_MOD).toShort()
    } else {
        ((readUnsignedShort() and Short.MAX_VALUE.toInt()) - Smart.SHORT_MOD).toShort()
    }
}

public fun ByteBuf.readUnsignedShortSmart(): Short {
    val peek = getByte(readerIndex()).toInt()
    return if (peek >= 0) {
        readUnsignedByte()
    } else {
        (readUnsignedShort() and Short.MAX_VALUE.toInt()).toShort()
    }
}

public fun ByteBuf.readIncrShortSmart(): Int {
    var total = 0
    var cur = readUnsignedShortSmart()
    while (cur == Short.MAX_VALUE) {
        total += Short.MAX_VALUE.toInt()
        cur = readUnsignedShortSmart()
    }
    total += cur
    return total
}

public fun ByteBuf.readIntSmart(): Int {
    val peek = getByte(readerIndex()).toInt()
    return if (peek >= 0) {
        readUnsignedShort() - Smart.SHORT_MOD
    } else {
        (readInt() and Int.MAX_VALUE) - Smart.INT_MOD
    }
}

public fun ByteBuf.readUnsignedIntSmart(): Int {
    val peek = getByte(readerIndex()).toInt()
    return if (peek >= 0) {
        readUnsignedShort()
    } else {
        readInt() and Int.MAX_VALUE
    }
}

public fun ByteBuf.readNullableUnsignedIntSmart(): Int? {
    val peek = getByte(readerIndex()).toInt()
    return if (peek >= 0) {
        val result = readUnsignedShort()
        return if (result == Short.MAX_VALUE.toInt()) null else result
    } else {
        readInt() and Int.MAX_VALUE
    }
}

public fun ByteBuf.readVarInt(): Int {
    var prev = 0
    var temp = readByte().toInt()
    while (temp < 0) {
        prev = prev or (temp and Byte.MAX_VALUE.toInt()) shl (Byte.SIZE_BITS - 1)
        temp = readByte().toInt()
    }
    return prev or temp
}

public fun ByteBuf.readString(charset: Charset = windows1252): String {
    val end = forEachByte(ByteProcessor.FIND_NUL)
    if (end == -1) throw IOException("String does not terminate.")
    val value = toString(readerIndex(), end - readerIndex(), charset)
    readerIndex(end + 1)
    return value
}

public fun ByteBuf.readVersionedString(charset: Charset = windows1252, expectedVersion: Int = 0): String {
    val actualVersion = readUnsignedByte().toInt()
    if (actualVersion != expectedVersion) throw IOException("Expected version number did not match actual version.")
    return readString(charset)
}

public fun ByteBuf.readBytesAdd(length: Int): ByteArray = readBytesAdd(ByteArray(length))

public fun ByteBuf.readBytesAdd(dest: ByteArray): ByteArray = dest.apply {
    readBytes(this)
}.map { (it - HALF_BYTE).toByte() }.toByteArray()

public fun ByteBuf.readBytesReversedAdd(length: Int): ByteArray = readBytesReversedAdd(ByteArray(length))

public fun ByteBuf.readBytesReversedAdd(dest: ByteArray): ByteArray = dest.apply {
    readBytes(this)
}.map { (it - HALF_BYTE).toByte() }.reversed().toByteArray()

public fun ByteBuf.readBytesReversed(length: Int): ByteArray = readBytesReversed(ByteArray(length))

public fun ByteBuf.readBytesReversed(dest: ByteArray): ByteArray = dest.apply {
    readBytes(this)
}.reversed().toByteArray()

public fun ByteBuf.writeByteNeg(value: Int): ByteBuf = writeByte(-value)

public fun ByteBuf.writeByteAdd(value: Int): ByteBuf = writeByte(value + HALF_BYTE)

public fun ByteBuf.writeByteSub(value: Int): ByteBuf = writeByte(HALF_BYTE - value)

public fun ByteBuf.writeShortAdd(value: Int): ByteBuf {
    writeByte(value shr Byte.SIZE_BITS)
    writeByte(value + HALF_BYTE)
    return this
}

public fun ByteBuf.writeShortLEAdd(value: Int): ByteBuf {
    writeByte(value + HALF_BYTE)
    writeByte(value shr Byte.SIZE_BITS)
    return this
}

public fun ByteBuf.writeMediumLME(value: Int): ByteBuf {
    writeShortLE(value shr Byte.SIZE_BITS)
    writeByte(value)
    return this
}

public fun ByteBuf.writeMediumRME(value: Int): ByteBuf {
    writeByte(value shr Short.SIZE_BITS)
    writeShortLE(value)
    return this
}

public fun ByteBuf.writeIntME(value: Int): ByteBuf {
    writeShortLE(value shr Short.SIZE_BITS)
    writeShortLE(value)
    return this
}

public fun ByteBuf.writeIntIME(value: Int): ByteBuf {
    writeShort(value)
    writeShort(value shr Short.SIZE_BITS)
    return this
}

public fun ByteBuf.writeSmallLong(value: Long): ByteBuf {
    writeMedium((value shr Medium.SIZE_BITS).toInt())
    writeMedium(value.toInt())
    return this
}

public fun ByteBuf.writeShortSmart(value: Int): ByteBuf = when (value) {
    in Smart.MIN_BYTE_VALUE..Smart.MAX_BYTE_VALUE -> writeByte(value + Smart.BYTE_MOD)
    in Smart.MIN_SHORT_VALUE..Smart.MAX_SHORT_VALUE -> {
        writeShort((Short.MAX_VALUE + 1) or (value + Smart.SHORT_MOD))
    }
    else -> throw IllegalArgumentException(
        "Value should be between ${Smart.MIN_SHORT_VALUE} and ${Smart.MAX_SHORT_VALUE}, but was $value."
    )
}

public fun ByteBuf.writeIncrShortSmart(value: Int): ByteBuf {
    var remaining = value
    while (remaining >= Short.MAX_VALUE.toInt()) {
        writeUnsignedShortSmart(Short.MAX_VALUE.toInt())
        remaining -= Short.MAX_VALUE.toInt()
    }
    writeUnsignedShortSmart(remaining)
    return this
}

public fun ByteBuf.writeUnsignedShortSmart(value: Int): ByteBuf = when (value) {
    in USmart.MIN_BYTE_VALUE..USmart.MAX_BYTE_VALUE -> writeByte(value)
    in USmart.MIN_SHORT_VALUE..USmart.MAX_SHORT_VALUE -> writeShort((Short.MAX_VALUE + 1) or value)
    else -> throw IllegalArgumentException(
        "Value should be between ${USmart.MIN_SHORT_VALUE} and ${USmart.MAX_SHORT_VALUE}, but was $value."
    )
}

public fun ByteBuf.writeIntSmart(value: Int): ByteBuf = when (value) {
    in Smart.MIN_SHORT_VALUE..Smart.MAX_SHORT_VALUE -> writeShort(value + Smart.SHORT_MOD)
    in Smart.MIN_INT_VALUE..Smart.MAX_INT_VALUE -> {
        writeInt(Int.MIN_VALUE or (value + Smart.INT_MOD))
    }
    else -> throw IllegalArgumentException(
        "Value should be between ${Smart.MIN_INT_VALUE} and ${Smart.MAX_INT_VALUE}, but was $value."
    )
}

public fun ByteBuf.writeUnsignedIntSmart(value: Int): ByteBuf = when (value) {
    in USmart.MIN_SHORT_VALUE..USmart.MAX_SHORT_VALUE -> writeShort(value)
    in USmart.MIN_INT_VALUE..USmart.MAX_INT_VALUE -> writeInt(Int.MIN_VALUE or value)
    else -> throw IllegalArgumentException(
        "Value should be between ${USmart.MIN_INT_VALUE} and ${USmart.MAX_INT_VALUE}, but was $value."
    )
}

public fun ByteBuf.writeNullableUnsignedIntSmart(value: Int?): ByteBuf = when (value) {
    null -> writeShort(USmart.MAX_SHORT_VALUE)
    in USmart.MIN_SHORT_VALUE until USmart.MAX_SHORT_VALUE -> writeShort(value)
    in USmart.MIN_INT_VALUE..USmart.MAX_INT_VALUE -> writeInt(Int.MIN_VALUE or value)
    else -> throw IllegalArgumentException(
        "Value should be between ${USmart.MIN_INT_VALUE} and ${USmart.MAX_INT_VALUE}, but was $value."
    )
}

public fun ByteBuf.writeVarInt(value: Int): ByteBuf {
    if (value and -128 != 0) {
        if (value and -16384 != 0) {
            if (value and -2097152 != 0) {
                if (value and -268435456 != 0) {
                    writeByte(value.ushr(4 * (Byte.SIZE_BITS - 1)) or (Byte.MAX_VALUE.toInt() + 1))
                }
                writeByte(value.ushr(3 * (Byte.SIZE_BITS - 1)) or (Byte.MAX_VALUE.toInt() + 1))
            }
            writeByte(value.ushr(2 * (Byte.SIZE_BITS - 1)) or (Byte.MAX_VALUE.toInt() + 1))
        }
        writeByte(value.ushr((Byte.SIZE_BITS - 1)) or (Byte.MAX_VALUE.toInt() + 1))
    }
    writeByte(value and Byte.MAX_VALUE.toInt())
    return this
}

public fun ByteBuf.writeString(value: String, charset: Charset = windows1252): ByteBuf {
    writeCharSequence(value, charset)
    writeByte(0)
    return this
}

public fun ByteBuf.writeVersionedString(value: String, charset: Charset = windows1252, version: Int = 0): ByteBuf {
    writeByte(version)
    writeString(value, charset)
    return this
}

public fun ByteBuf.writeBytesAdd(src: ByteArray): ByteBuf = writeBytes(src.map {
    (it + HALF_BYTE).toByte()
}.toByteArray())

public fun ByteBuf.writeBytesAdd(src: ByteBuf): ByteBuf {
    for (i in src.readerIndex() until src.writerIndex()) {
        writeByte(src.getByte(i) + HALF_BYTE)
    }
    return this
}

public fun ByteBuf.writeBytesReversedAdd(src: ByteArray): ByteBuf = writeBytes(src.map {
    (it + HALF_BYTE).toByte()
}.reversed().toByteArray())

public fun ByteBuf.writeBytesReversedAdd(src: ByteBuf): ByteBuf {
    for (i in src.writerIndex() - 1 downTo src.readerIndex()) {
        writeByte(src.getByte(i) + HALF_BYTE)
    }
    return this
}

public fun ByteBuf.writeBytesReversed(src: ByteBuf): ByteBuf {
    for (i in src.writerIndex() - 1 downTo src.readerIndex()) {
        writeByte(src.getByte(i).toInt())
    }
    return this
}