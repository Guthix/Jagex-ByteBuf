/*
 * Copyright (C) 2019 Guthix
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.guthix.buffer

import io.netty.buffer.ByteBuf
import java.lang.IllegalArgumentException
import java.nio.charset.Charset

private const val HALF_BYTE = 128.toByte()

private val cp1252 = Charset.availableCharsets()["windows-1252"] ?: throw IllegalStateException(
    "Could not find CP1252 character set"
)

private val cesu8 = Charset.availableCharsets()["CESU-8"] ?: throw IllegalStateException(
    "Could not find CESU-8 character set"
)

fun ByteBuf.getByteNEG(index: Int) = (-getByte(index)).toByte()

fun ByteBuf.getByteADD(index: Int) = (getByte(index) - HALF_BYTE).toByte()

fun ByteBuf.getByteSUB(index: Int) = (HALF_BYTE - getByte(index)).toByte()

fun ByteBuf.getUnsignedByteNEG(index: Int) = (-getUnsignedByte(index)).toShort()

fun ByteBuf.getUnsignedByteADD(index: Int) = (getUnsignedByte(index) - HALF_BYTE).toShort()

fun ByteBuf.getUnsignedByteSUB(index: Int) = (HALF_BYTE - getUnsignedByte(index)).toShort()

fun ByteBuf.getShortADD(index: Int) = (getShort(index) - HALF_BYTE).toShort()

fun ByteBuf.getShortLEADD(index: Int) = (getShortLE(index) - HALF_BYTE).toShort()

fun ByteBuf.getUnsignedShortADD(index: Int) = getUnsignedShort(index) - HALF_BYTE

fun ByteBuf.getUnsignedShortLEADD(index: Int) = getUnsignedShortLE(index) - HALF_BYTE

fun ByteBuf.getIntPDPE(index: Int) = (getShortLE(index).toInt() shl 16) or getUnsignedShortLE(index + 1)

fun ByteBuf.getIntIPDPE(index: Int) = getUnsignedShort(index) or (getShort(index + 1).toInt() shl 16)

fun ByteBuf.getUnsignedIntPDPE(index: Int) = (getUnsignedShortLE(index) shl 16) or getUnsignedShortLE(index + 1)

fun ByteBuf.getUnsignedIntIPDPE(index: Int) = getUnsignedShort(index) or (getUnsignedShort(index + 1) shl 16)

fun ByteBuf.getSmallLong(index: Int) = (getMedium(index).toLong() shl 24) or getUnsignedMedium(index + 1).toLong()

fun ByteBuf.getUnsignedSmallLong(index: Int) = (getUnsignedMedium(index).toLong() shl 24) or
        getUnsignedMedium(index + 1).toLong()

fun ByteBuf.getSmallSmart(index: Int): Int {
    val peak = getUnsignedByte(index)
    return if(peak < 128) peak - 64 else getUnsignedShort(index) - 49152
}

fun ByteBuf.getUnsignedSmallSmart(index: Int): Int {
    val peak = getUnsignedByte(index)
    return if(peak < 128) peak.toInt() else getUnsignedShort(index) - 32768
}

fun ByteBuf.getLargeSmart(index: Int) = if (getByte(readerIndex()) < 0) {
    getInt(index)
} else {
    getUnsignedShort(index)
}

fun ByteBuf.getVarInt(index: Int): Int {
    var temp = getByte(index).toInt()
    var prev = 0
    var i = index + 1
    while (temp < 0) {
        prev = prev or (temp and 127) shl 7
        temp = getByte(i++).toInt()
    }
    return prev or temp
}

fun ByteBuf.getIncrSmallSmart(index: Int): Int {
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

fun ByteBuf.getStringCP1252(index: Int): String {
    var i = index
    while(getByte(i++).toInt() != 0) { }
    val size = i - index - 1
    return getCharSequence(index, size, cp1252).toString()
}

fun ByteBuf.getStringCP1252Nullable(index: Int): String? {
    if(getByte(index).toInt() == 0) {
        return null
    }
    return getStringCP1252(index)
}

fun ByteBuf.getString0CP1252(index: Int): String {
    check(getByte(index).toInt() == 0) { "First byte is not 0." }
    return getStringCP1252(index + 1)
}

fun ByteBuf.setByteNEG(index: Int, value: Int) = setByte(index, -value)

fun ByteBuf.setByteADD(index: Int, value: Int) = setByte(index, value + HALF_BYTE)

fun ByteBuf.seteByteSUB(index: Int, value: Int) = setByte(index, HALF_BYTE - value)

fun ByteBuf.setShortADD(index: Int, value: Int) = setShort(index, value + HALF_BYTE)

fun ByteBuf.setShortLEADD(index: Int, value: Int) = setShortLE(index, value + HALF_BYTE)

fun ByteBuf.setIntPDPE(index: Int, value: Int): ByteBuf {
    setShortLE(index, value shr 16)
    setShortLE(index + 1, value)
    return this
}

fun ByteBuf.setIntIPDPE(index: Int, value: Int): ByteBuf {
    setShort(index, value)
    setShort(index + 1, value shr 16)
    return this
}

fun ByteBuf.setSmallLong(index: Int, value: Int): ByteBuf {
    setMedium(index, value shr 24)
    setMedium(index + 1, value)
    return this
}

fun ByteBuf.setSmallSmart(index: Int, value: Int) = when (value) {
    in 0 until 128 -> {
        setByte(index, value)
        Byte.SIZE_BYTES
    }
    in 0 until 32768 -> {
        setShort(index, value + 32768)
        Short.SIZE_BYTES
    }
    else -> throw IllegalArgumentException("Can't set value bigger than 32767.")
}

fun ByteBuf.setLargeSmart(index: Int, value: Int) = if(value <= Short.MAX_VALUE) {
    setShort(index, value)
    Short.SIZE_BYTES
} else {
    setInt(index, value)
    Int.SIZE_BYTES
}

fun ByteBuf.setVarInt(index: Int, value: Int): Int {
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

fun ByteBuf.setStringCP1252(index: Int, value: String): ByteBuf {
    setCharSequence(index, value, cp1252)
    setByte(index + value.length + 1, 0)
    return this
}

fun ByteBuf.setString0CP1252(index: Int, value: String): ByteBuf {
    writeByte(index)
    setStringCP1252(index + 1, value)
    return this
}

fun ByteBuf.setStringCESU8(index: Int, value: String): ByteBuf {
    setByte(index, 0)
    val byteWritten = setVarInt(index + 1, value.length)
    setCharSequence(index + byteWritten, value, cesu8)
    return this
}

fun ByteBuf.readByteNEG() = (-readByte()).toByte()

fun ByteBuf.readByteADD() = (readByte() - HALF_BYTE).toByte()

fun ByteBuf.readByteSUB() = (HALF_BYTE - readByte()).toByte()

fun ByteBuf.readUnsignedByteNEG() = (-readUnsignedByte()).toShort()

fun ByteBuf.readUnsignedByteADD() = (readUnsignedByte() - HALF_BYTE).toShort()

fun ByteBuf.readUnsignedByteSUB() = (HALF_BYTE - readUnsignedByte()).toShort()

fun ByteBuf.readShortADD() = (readShort() - HALF_BYTE).toShort()

fun ByteBuf.readShortLEADD() = (readShortLE() - HALF_BYTE).toShort()

fun ByteBuf.readUnsignedShortADD() = readUnsignedShort() - HALF_BYTE

fun ByteBuf.readUnsignedShortLEADD() = readUnsignedShortLE() - HALF_BYTE

fun ByteBuf.readIntPDPE() = (readShortLE().toInt() shl 16) or readUnsignedShortLE()

fun ByteBuf.readIntIPDPE() = readUnsignedShort() or (readShort().toInt() shl 16)

fun ByteBuf.readUnsignedIntPDPE() = (readUnsignedShortLE() shl 16) or readUnsignedShortLE()

fun ByteBuf.readUnsignedIntIPDPE() = readUnsignedShort() or (readUnsignedShort() shl 16)

fun ByteBuf.readSmallLong() = (readMedium().toLong() shl 24) or readUnsignedMedium().toLong()

fun ByteBuf.readUnsignedSmallLong() = (readUnsignedMedium().toLong() shl 24) or readUnsignedMedium().toLong()

fun ByteBuf.readSmallSmart(): Int {
    val peak = getUnsignedByte(readerIndex())
    return if(peak < 128) peak - 64 else readUnsignedShort() - 49152
}

fun ByteBuf.readUnsignedSmallSmart(): Int {
    val peak = getUnsignedByte(readerIndex())
    return if(peak < 128) peak.toInt() else readUnsignedShort() - 32768
}

fun ByteBuf.readLargeSmart() = if (getByte(readerIndex()) < 0) {
    readInt()
} else {
    readUnsignedShort()
}

fun ByteBuf.readVarInt(): Int {
    var prev = 0
    var temp = readByte().toInt()
    while (temp < 0) {
        prev = prev or (temp and 127) shl 7
        temp = readByte().toInt()
    }
    return prev or temp
}

fun ByteBuf.readIncrSmallSmart(): Int {
    var total = 0
    var cur = readUnsignedSmallSmart()
    while (cur == 32767) {
        total += 32767
        cur = readUnsignedSmallSmart()
    }
    total += cur
    return total
}

fun ByteBuf.readStringCP1252(): String {
    val current = readerIndex()
    while(readByte().toInt() != 0) { }
    val size = readerIndex() - current - 1
    readerIndex(current)
    return readCharSequence(size, cp1252).toString()
}

fun ByteBuf.readStringCP1252Nullable(): String? {
    if(getByte(readerIndex()).toInt() == 0) {
        readerIndex(readerIndex() + 1)
        return null
    }
    return readStringCP1252()
}

fun ByteBuf.readString0CP1252(): String {
    check(readByte().toInt() == 0) { "First byte is not 0." }
    return readStringCP1252()
}

fun ByteBuf.readStringCESU8(): String {
    check(readByte().toInt() == 0) { "First byte is not 0." }
    val length = readVarInt()
    return readCharSequence(length, cesu8).toString()
}

fun ByteBuf.writeByteNEG(value: Int) = writeByte(-value)

fun ByteBuf.writeByteADD(value: Int) = writeByte(value + HALF_BYTE)

fun ByteBuf.writeByteSUB(value: Int) = writeByte(HALF_BYTE - value)

fun ByteBuf.writeShortADD(value: Int) = writeShort(value + HALF_BYTE)

fun ByteBuf.writeShortLEADD(value: Int) = writeShortLE(value + HALF_BYTE)

fun ByteBuf.writeIntPDPE(value: Int): ByteBuf {
    writeShortLE(value shr 16)
    writeShortLE(value)
    return this
}

fun ByteBuf.writeIntIPDPE(value: Int): ByteBuf {
    writeShort(value)
    writeShort(value shr 16)
    return this
}

fun ByteBuf.writeSmallLong(value: Int): ByteBuf {
    writeMedium(value shr 24)
    writeMedium(value)
    return this
}

fun ByteBuf.writeSmallSmart(value: Int): ByteBuf {
    when (value) {
        in 0 until 128 -> writeByte(value)
        in 0 until 32768 -> writeShort(value + 32768)
        else -> throw IllegalArgumentException("Can't write value bigger than 32767.")
    }
    return this
}

fun ByteBuf.writeLargeSmart(value: Int): ByteBuf {
    if(value <= Short.MAX_VALUE) {
        writeShort(value)
    } else {
        writeInt(value)
    }
    return this
}

fun ByteBuf.writeVarInt(value: Int): ByteBuf {
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

fun ByteBuf.writeStringCP1252(value: String): ByteBuf {
    writeCharSequence(value, cp1252)
    writeByte(0)
    return this
}

fun ByteBuf.writeString0CP1252(value: String): ByteBuf {
    writeByte(0)
    writeStringCP1252(value)
    return this
}

fun ByteBuf.writeStringCESU8(value: String): ByteBuf {
    writeByte(0)
    writeVarInt(value.length)
    writeCharSequence(value, cesu8)
    return this
}