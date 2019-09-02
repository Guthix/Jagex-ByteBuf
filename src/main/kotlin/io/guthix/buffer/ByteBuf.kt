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
