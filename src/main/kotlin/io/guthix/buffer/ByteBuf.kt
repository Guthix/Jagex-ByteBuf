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
import java.nio.charset.Charset
import kotlin.Long

private const val HALF_BYTE = 128

private val cp1252 = Charset.availableCharsets()["windows-1252"] ?: throw IllegalStateException(
    "Could not find CP1252 character set."
)

private val cesu8 = Charset.availableCharsets()["CESU-8"] ?: throw IllegalStateException(
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

public fun ByteBuf.getSmallSmart(index: Int): Int {
    val peak = getUnsignedByte(index)
    return if (peak < 128) peak - 64 else getUnsignedShort(index) - 49152
}

public fun ByteBuf.getUnsignedSmallSmart(index: Int): Int {
    val peak = getUnsignedByte(index)
    return if (peak < 128) peak.toInt() else getUnsignedShort(index) - 32768
}

public fun ByteBuf.getLargeSmart(index: Int): Int = if (getByte(readerIndex()) < 0) {
    getInt(index) and Integer.MAX_VALUE
} else {
    getUnsignedShort(index)
}

public fun ByteBuf.getNullableLargeSmart(index: Int): Int? = if (getByte(readerIndex()) < 0) {
    getInt(index) and Integer.MAX_VALUE
} else {
    val result = getUnsignedShort(index)
    if (result == 32767) null else result
}

public fun ByteBuf.getVarInt(index: Int): Int {
    var temp = getByte(index).toInt()
    var prev = 0
    var i = index + 1
    while (temp < 0) {
        prev = prev or (temp and 127) shl 7
        temp = getByte(i++).toInt()
    }
    return prev or temp
}

public fun ByteBuf.getIncrSmallSmart(index: Int): Int {
    var total = 0
    var i = index
    var cur = getUnsignedSmallSmart(i++)
    while (cur == 32767) {
        total += 32767
        cur = getUnsignedSmallSmart(i++)
    }
    total += cur
    return total
}

public fun ByteBuf.getStringCP1252(index: Int): String {
    var i = index
    while (getByte(i++).toInt() != 0) { }
    val size = i - index - 1
    return getCharSequence(index, size, cp1252).toString()
}

public fun ByteBuf.getStringCP1252Nullable(index: Int): String? {
    if (getByte(index).toInt() == 0) {
        return null
    }
    return getStringCP1252(index)
}

public fun ByteBuf.getString0CP1252(index: Int): String {
    check(getByte(index).toInt() == 0) { "First byte is not 0." }
    return getStringCP1252(index + 1)
}

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
    setByte(index + 1, value + HALF_BYTE)
    return this
}

public fun ByteBuf.setShortLEAdd(index: Int, value: Int): ByteBuf {
    setByte(index, value + HALF_BYTE)
    setByte(index + 1, value shr Byte.SIZE_BITS)
    return this
}

public fun ByteBuf.setMediumLME(index: Int, value: Int): ByteBuf {
    setShortLE(index, value shr Byte.SIZE_BITS)
    setByte(index + 2, value)
    return this
}

public fun ByteBuf.setMediumRME(index: Int, value: Int): ByteBuf {
    setByte(index, value shr Short.SIZE_BITS)
    setShortLE(index + 1, value)
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

public fun ByteBuf.setSmallSmart(index: Int, value: Short): Int = when (value) {
    in 0 until 128 -> {
        setByte(index, value.toInt())
        Byte.SIZE_BYTES
    }
    in 128 until 32768 -> {
        setShort(index, value + 32768)
        Short.SIZE_BYTES
    }
    else -> throw IllegalArgumentException("Can't set negative value.")
}

public fun ByteBuf.setLargeSmart(index: Int, value: Int): Int = if (value <= Short.MAX_VALUE) {
    setShort(index, value)
    Short.SIZE_BYTES
} else {
    setInt(index, value)
    Int.SIZE_BYTES
}

public fun ByteBuf.setNullableLargeSmart(index: Int, value: Int?): Int = when {
    value == null -> {
        setShort(index, 32767)
        Short.SIZE_BYTES
    }
    value < Short.MAX_VALUE -> {
        setShort(index, value)
        Short.SIZE_BYTES
    }
    else -> {
        setInt(index, value)
        Int.SIZE_BYTES
    }
}

public fun ByteBuf.setVarInt(index: Int, value: Int): Int {
    var i = index
    if (value and -128 != 0) {
        if (value and -16384 != 0) {
            if (value and -2097152 != 0) {
                if (value and -268435456 != 0) {
                    setByte(i++, value.ushr(28) or 128)
                }
                setByte(i++, value.ushr(21) or 128)
            }
            setByte(i++, value.ushr(14) or 128)
        }
        setByte(i++, value.ushr(7) or 128)
    }
    setByte(i++, value and 127)
    return i - index
}

public fun ByteBuf.setIncrSmallSmart(index: Int, value: Int): ByteBuf {
    var remaining = value
    var curIndex = index
    while (remaining > 32767) {
        setSmallSmart(curIndex,32767)
        curIndex += 2
        remaining -= 32767
    }
    setSmallSmart(curIndex, remaining.toShort())
    return this
}

public fun ByteBuf.setStringCP1252(index: Int, value: String): ByteBuf {
    setCharSequence(index, value, cp1252)
    setByte(index + value.length + 1, 0)
    return this
}

public fun ByteBuf.setString0CP1252(index: Int, value: String): ByteBuf {
    writeByte(index)
    setStringCP1252(index + 1, value)
    return this
}

public fun ByteBuf.setStringCESU8(index: Int, value: String): ByteBuf {
    setByte(index, 0)
    val byteWritten = setVarInt(index + 1, value.length)
    setCharSequence(index + byteWritten, value, cesu8)
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

public fun ByteBuf.readSmallSmart(): Int {
    val peak = getUnsignedByte(readerIndex())
    return if (peak < 128) readByte() - 64 else readUnsignedShort() - 49152
}

public fun ByteBuf.readUnsignedSmallSmart(): Int {
    val peak = getUnsignedByte(readerIndex())
    return if (peak < 128) readUnsignedByte().toInt() else readUnsignedShort() - 32768
}

public fun ByteBuf.readLargeSmart(): Int = if (getByte(readerIndex()) < 0) {
    readInt() and Integer.MAX_VALUE
} else {
    readUnsignedShort()
}

public fun ByteBuf.readNullableLargeSmart(): Int? = if (getByte(readerIndex()) < 0) {
    readInt() and Integer.MAX_VALUE
} else {
    val result = readUnsignedShort()
    if (result == 32767) null else result
}

public fun ByteBuf.readVarInt(): Int {
    var prev = 0
    var temp = readByte().toInt()
    while (temp < 0) {
        prev = prev or (temp and 127) shl 7
        temp = readByte().toInt()
    }
    return prev or temp
}

public fun ByteBuf.readIncrSmallSmart(): Int {
    var total = 0
    var cur = readUnsignedSmallSmart()
    while (cur == 32767) {
        total += 32767
        cur = readUnsignedSmallSmart()
    }
    total += cur
    return total
}

public fun ByteBuf.readStringCP1252(): String {
    val current = readerIndex()
    while (readByte().toInt() != 0) { }
    val size = readerIndex() - current - 1
    readerIndex(current)
    val str = readCharSequence(size, cp1252).toString()
    readerIndex(readerIndex() + 1) // read the 0 value
    return str
}

public fun ByteBuf.readStringCP1252Nullable(): String? {
    if (getByte(readerIndex()).toInt() == 0) {
        readerIndex(readerIndex() + 1)
        return null
    }
    return readStringCP1252()
}

public fun ByteBuf.readString0CP1252(): String {
    check(readByte().toInt() == 0) { "First byte is not 0." }
    return readStringCP1252()
}

public fun ByteBuf.readStringCESU8(): String {
    check(readByte().toInt() == 0) { "First byte is not 0." }
    val length = readVarInt()
    return readCharSequence(length, cesu8).toString()
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
    writeMedium((value shr 24).toInt())
    writeMedium(value.toInt())
    return this
}

public fun ByteBuf.writeSmallSmart(value: Short): ByteBuf {
    when (value) {
        in 0 until 128 -> writeByte(value.toInt())
        in 128 until 32768 -> writeShort(value + 32768)
        else -> throw IllegalArgumentException("Can't write negative value.")
    }
    return this
}

public fun ByteBuf.writeLargeSmart(value: Int): ByteBuf {
    if (value <= Short.MAX_VALUE) {
        writeShort(value)
    } else {
        writeInt(value)
    }
    return this
}

public fun ByteBuf.writeNullableLargeSmart(value: Int?): ByteBuf = when {
    value == null -> {
        writeShort(32767)
    }
    value < Short.MAX_VALUE -> {
        writeShort(value)
    }
    else -> {
        writeInt(value)
    }
}

public fun ByteBuf.writeVarInt(value: Int): ByteBuf {
    if (value and -128 != 0) {
        if (value and -16384 != 0) {
            if (value and -2097152 != 0) {
                if (value and -268435456 != 0) {
                    writeByte(value.ushr(28) or 128)
                }
                writeByte(value.ushr(21) or 128)
            }
            writeByte(value.ushr(14) or 128)
        }
        writeByte(value.ushr(7) or 128)
    }
    writeByte(value and 127)
    return this
}

public fun ByteBuf.writeIncrSmallSmart(value: Int): ByteBuf {
    var remaining = value
    while (remaining > 32767) {
        writeSmallSmart(32767)
        remaining -= 32767
    }
    writeSmallSmart(remaining.toShort())
    return this
}

public fun ByteBuf.writeStringCP1252(value: String): ByteBuf {
    writeCharSequence(value, cp1252)
    writeByte(0)
    return this
}

public fun ByteBuf.writeString0CP1252(value: String): ByteBuf {
    writeByte(0)
    writeStringCP1252(value)
    return this
}

public fun ByteBuf.writeStringCESU8(value: String): ByteBuf {
    writeByte(0)
    writeVarInt(value.length)
    writeCharSequence(value, cesu8)
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