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

fun ByteBuf.getStringCp1252(index: Int): String {
    var i = index
    while(getByte(i++).toInt() != 0) { }
    val size = i - index - 1
    return getCharSequence(index, size, cp1252).toString()
}

fun ByteBuf.getStringCp1252Nullable(index: Int): String? {
    if(getByte(index).toInt() == 0) {
        return null
    }
    return getStringCp1252(index)
}

fun ByteBuf.getString0Cp1252(index: Int): String {
    if(getByte(index).toInt() != 0) throw IllegalStateException("First byte is not 0.")
    return getStringCp1252(index + 1)
}

fun ByteBuf.setByteNEG(index: Int, value: Int) = setByte(index, -value)

fun ByteBuf.setByteADD(index: Int, value: Int) = setByte(index, value + HALF_BYTE)

fun ByteBuf.seteByteSUB(index: Int, value: Int) = setByte(index, HALF_BYTE - value)

fun ByteBuf.setShortADD(index: Int, value: Int) = setShort(index, value + HALF_BYTE)

fun ByteBuf.setShortLEADD(index: Int, value: Int) = setShortLE(index, value + HALF_BYTE)

fun ByteBuf.setIntPDPE(index: Int, value: Int) {
    setShortLE(index, value shr 16)
    setShortLE(index + 1, value)
}

fun ByteBuf.setIntIPDPE(index: Int, value: Int) {
    setShort(index, value)
    setShort(index + 1, value shr 16)
}

fun ByteBuf.setSmallLong(index: Int, value: Int) {
    setMedium(index, value shr 24)
    setMedium(index + 1, value)
}

fun ByteBuf.setStringCp1252(index: Int, value: String) {
    setCharSequence(index, value, cp1252)
    setByte(index + value.length + 1, 0)
}

fun ByteBuf.setString0Cp1252(index: Int, value: String) {
    writeByte(index)
    setStringCp1252(index + 1, value)
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

fun ByteBuf.readStringCp1252(): String {
    val current = readerIndex()
    while(readByte().toInt() != 0) { }
    val size = readerIndex() - current - 1
    readerIndex(current)
    return readCharSequence(size, cp1252).toString()
}

fun ByteBuf.readStringCp1252Nullable(): String? {
    if(getByte(readerIndex()).toInt() == 0) {
        readerIndex(readerIndex() + 1)
        return null
    }
    return readStringCp1252()
}

fun ByteBuf.readString0Cp1252(): String {
    if(readByte().toInt() != 0) throw IllegalStateException("First byte is not 0.")
    return readStringCp1252()
}




fun ByteBuf.writeByteNEG(value: Int) = writeByte(-value)

fun ByteBuf.writeByteADD(value: Int) = writeByte(value + HALF_BYTE)

fun ByteBuf.writeByteSUB(value: Int) = writeByte(HALF_BYTE - value)

fun ByteBuf.writeShortADD(value: Int) = writeShort(value + HALF_BYTE)

fun ByteBuf.writeShortLEADD(value: Int) = writeShortLE(value + HALF_BYTE)

fun ByteBuf.writeIntPDPE(value: Int) {
    writeShortLE(value shr 16)
    writeShortLE(value)
}

fun ByteBuf.writeIntIPDPE(value: Int) {
    writeShort(value)
    writeShort(value shr 16)
}

fun ByteBuf.writeSmallLong(value: Int) {
    writeMedium(value shr 24)
    writeMedium(value)
}

fun ByteBuf.writeStringCp1252(value: String) {
    writeCharSequence(value, cp1252)
    writeByte(0)
}

fun ByteBuf.writeString0Cp1252(value: String) {
    writeByte(0)
    writeStringCp1252(value)
}