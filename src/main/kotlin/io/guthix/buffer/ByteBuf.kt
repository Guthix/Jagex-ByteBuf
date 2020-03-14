/**
 * This file is part of Guthix Jagex-ByteBuf.
 *
 * Guthix Jagex-ByteBuf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Guthix Jagex-ByteBuf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */
@file:Suppress("unused", "ControlFlowWithEmptyBody")
package io.guthix.buffer

import io.netty.buffer.ByteBuf
import java.lang.IllegalArgumentException
import java.nio.charset.Charset

private const val HALF_BYTE = 128

private val cp1252 = Charset.availableCharsets()["windows-1252"] ?: throw IllegalStateException(
    "Could not find CP1252 character set."
)

private val cesu8 = Charset.availableCharsets()["CESU-8"] ?: throw IllegalStateException(
    "Could not find CESU-8 character set."
)

fun ByteBuf.getCharCP1252(index: Int) = String(byteArrayOf(getByte(index)), cp1252).first()

fun ByteBuf.getByteNeg(index: Int) = (-getByte(index)).toByte()

fun ByteBuf.getByteAdd(index: Int) = (getByte(index) - HALF_BYTE).toByte()

fun ByteBuf.getByteSub(index: Int) = (HALF_BYTE - getByte(index)).toByte()

fun ByteBuf.getUnsignedByteNeg(index: Int) = (-getUnsignedByte(index)).toShort()

fun ByteBuf.getUnsignedByteAdd(index: Int) = (getUnsignedByte(index) - HALF_BYTE).toShort()

fun ByteBuf.getUnsignedByteSub(index: Int) = (HALF_BYTE - getUnsignedByte(index)).toShort()

fun ByteBuf.getShortAdd(index: Int) =
    ((getByte(index).toInt() shl Byte.SIZE_BITS) or (getUnsignedByte(index + 1) - HALF_BYTE)).toShort()

fun ByteBuf.getShortAddLE(index: Int) =
    ((getUnsignedByte(index) - HALF_BYTE) or (getByte(index + 1).toInt() shl Byte.SIZE_BITS)).toShort()

fun ByteBuf.getUnsignedShortAdd(index: Int) =
    (getUnsignedByte(index).toInt() shl Byte.SIZE_BITS) or ((getUnsignedByte(index + 1) - HALF_BYTE) and 0xFF)

fun ByteBuf.getUnsignedShortAddLE(index: Int) =
    ((getUnsignedByte(index) - HALF_BYTE) and 0xFF) or (getUnsignedByte(index + 1).toInt() shl Byte.SIZE_BITS)

fun ByteBuf.getIntME(index: Int) = (getShortLE(index).toInt() shl 16) or getUnsignedShortLE(index + 1)

fun ByteBuf.getIntIME(index: Int) = getUnsignedShort(index) or (getShort(index + 1).toInt() shl 16)

fun ByteBuf.getUnsignedIntME(index: Int) = ((getUnsignedShortLE(index) shl 16) or
        getUnsignedShortLE(index + 1)).toLong()

fun ByteBuf.getUnsignedIntIME(index: Int) = (getUnsignedShort(index) or
        (getUnsignedShort(index + 1) shl 16)).toLong()

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
    getInt(index) and Integer.MAX_VALUE
} else {
    getUnsignedShort(index)
}

fun ByteBuf.getNullableLargeSmart(index: Int) = if (getByte(readerIndex()) < 0) {
    getInt(index) and Integer.MAX_VALUE
} else {
    val result = getUnsignedShort(index)
    if(result == 32767) null else result
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

fun ByteBuf.getBytesReversedAdd(index: Int, length: Int): ByteArray = getBytesReversedAdd(index, ByteArray(length))

fun ByteBuf.getBytesReversedAdd(index: Int, dest: ByteArray): ByteArray = dest.apply {
    getBytes(index, this)
}.map { (it - HALF_BYTE).toByte() }.reversed().toByteArray()

fun ByteBuf.setCharCP1252(index: Int, value: Char): ByteBuf = setByte(index, cp1252.encode(value.toString()).get().toInt())

fun ByteBuf.setByteNeg(index: Int, value: Int): ByteBuf = setByte(index, -value)

fun ByteBuf.setByteAdd(index: Int, value: Int): ByteBuf = setByte(index, value + HALF_BYTE)

fun ByteBuf.seteByteSub(index: Int, value: Int): ByteBuf = setByte(index, HALF_BYTE - value)

fun ByteBuf.setShortAdd(index: Int, value: Int): ByteBuf {
    setByte(index, value shr Byte.SIZE_BITS)
    setByte(index + 1, value + HALF_BYTE)
    return this
}

fun ByteBuf.setShortAddLE(index: Int, value: Int): ByteBuf {
    setByte(index, value + HALF_BYTE)
    setByte(index + 1, value shr Byte.SIZE_BITS)
    return this
}

fun ByteBuf.setIntME(index: Int, value: Int): ByteBuf {
    setShortLE(index, value shr Short.SIZE_BITS)
    setShortLE(index + 1, value)
    return this
}

fun ByteBuf.setIntIME(index: Int, value: Int): ByteBuf {
    setShort(index, value)
    setShort(index + 1, value shr Short.SIZE_BITS)
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
    in 128 until 32768 -> {
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

fun ByteBuf.setNullableLargeSmart(index: Int, value: Int?): Int = when {
    value == null -> {
        setShort(index, 32767)
        Short.SIZE_BYTES
    }
    value < Short.MAX_VALUE  -> {
        setShort(index, value)
        Short.SIZE_BYTES
    }
    else -> {
        setInt(index, value)
        Int.SIZE_BYTES
    }
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

fun ByteBuf.setBytesReversedAdd(index: Int, src: ByteArray): ByteBuf = setBytes(index, src.map {
    (it + HALF_BYTE).toByte()
}.reversed().toByteArray())

fun ByteBuf.setBytesReversedAdd(index: Int, src: ByteBuf): ByteBuf {
    var j = index
    for(i in src.writerIndex() - 1 downTo src.readerIndex()) {
        setByte(j, src.getByte(i) + HALF_BYTE)
        j++
    }
    return this
}

fun ByteBuf.readCharCP1252() = String(byteArrayOf(readByte()), cp1252).first()

fun ByteBuf.readByteNeg() = (-readByte()).toByte()

fun ByteBuf.readByteAdd() = (readByte() - HALF_BYTE).toByte()

fun ByteBuf.readByteSub() = (HALF_BYTE - readByte()).toByte()

fun ByteBuf.readUnsignedByteNeg() = (-readUnsignedByte()).toShort()

fun ByteBuf.readUnsignedByteAdd() = (readUnsignedByte() - HALF_BYTE).toShort()

fun ByteBuf.readUnsignedByteSub() = (HALF_BYTE - readUnsignedByte()).toShort()

fun ByteBuf.readShortAdd() = ((readByte().toInt() shl Byte.SIZE_BITS) or (readUnsignedByte() - HALF_BYTE)).toShort()

fun ByteBuf.readShortAddLE() = ((readUnsignedByte() - HALF_BYTE) or (readByte().toInt() shl Byte.SIZE_BITS)).toShort()

fun ByteBuf.readUnsignedShortAdd() =
    (readUnsignedByte().toInt() shl Byte.SIZE_BITS) or ((readUnsignedByte() - HALF_BYTE) and 0xFF)

fun ByteBuf.readUnsignedShortAddLE() =
    ((readUnsignedByte() - HALF_BYTE) and 0xFF) or (readUnsignedByte().toInt() shl Byte.SIZE_BITS)

fun ByteBuf.readIntME() = (readShortLE().toInt() shl 16) or readUnsignedShortLE()

fun ByteBuf.readIntIME() = readUnsignedShort() or (readShort().toInt() shl 16)

fun ByteBuf.readUnsignedIntME() = ((readUnsignedShortLE() shl 16) or readUnsignedShortLE()).toLong()

fun ByteBuf.readUnsignedIntIME() = (readUnsignedShort() or (readUnsignedShort() shl 16)).toLong()

fun ByteBuf.readSmallLong() = (readMedium().toLong() shl 24) or readUnsignedMedium().toLong()

fun ByteBuf.readUnsignedSmallLong() = (readUnsignedMedium().toLong() shl 24) or readUnsignedMedium().toLong()

fun ByteBuf.readSmallSmart(): Int {
    val peak = getUnsignedByte(readerIndex())
    return if(peak < 128) readByte() - 64 else readUnsignedShort() - 49152
}

fun ByteBuf.readUnsignedSmallSmart(): Int {
    val peak = getUnsignedByte(readerIndex())
    return if(peak < 128) readUnsignedByte().toInt() else readUnsignedShort() - 32768
}

fun ByteBuf.readLargeSmart() = if (getByte(readerIndex()) < 0) {
    readInt() and Integer.MAX_VALUE
} else {
    readUnsignedShort()
}

fun ByteBuf.readNullableLargeSmart() = if (getByte(readerIndex()) < 0) {
    readInt() and Integer.MAX_VALUE
} else {
    val result = readUnsignedShort()
    if(result == 32767) null else result
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
    val str = readCharSequence(size, cp1252).toString()
    readerIndex(readerIndex() + 1) // read the 0 value
    return str
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

fun ByteBuf.readBytesReversedAdd(length: Int): ByteArray = readBytesReversedAdd(ByteArray(length))

fun ByteBuf.readBytesReversedAdd(dest: ByteArray): ByteArray = dest.apply {
    readBytes(this)
}.map { (it - HALF_BYTE).toByte() }.reversed().toByteArray()

fun ByteBuf.writeCharCP1252(value: Char): ByteBuf = writeByte(cp1252.encode(value.toString()).get().toInt())

fun ByteBuf.writeByteNeg(value: Int): ByteBuf = writeByte(-value)

fun ByteBuf.writeByteAdd(value: Int): ByteBuf = writeByte(value + HALF_BYTE)

fun ByteBuf.writeByteSub(value: Int): ByteBuf = writeByte(HALF_BYTE - value)

fun ByteBuf.writeShortAdd(value: Int): ByteBuf {
    writeByte(value shr Byte.SIZE_BITS)
    writeByte(value + HALF_BYTE)
    return this
}

fun ByteBuf.writeShortAddLE(value: Int): ByteBuf {
    writeByte(value + HALF_BYTE)
    writeByte(value shr Byte.SIZE_BITS)
    return this
}

fun ByteBuf.writeIntME(value: Int): ByteBuf {
    writeShortLE(value shr 16)
    writeShortLE(value)
    return this
}

fun ByteBuf.writeIntIME(value: Int): ByteBuf {
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
        in 128 until 32768 -> writeShort(value + 32768)
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

fun ByteBuf.writeNullableLargeSmart(value: Int?): ByteBuf = when {
    value == null -> {
        writeShort(32767)
    }
    value < Short.MAX_VALUE  -> {
        writeShort(value)
    }
    else -> {
        writeInt(value)
    }
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

fun ByteBuf.writeBytesReversedAdd(src: ByteArray): ByteBuf = writeBytes(src.map {
    (it + HALF_BYTE).toByte()
}.reversed().toByteArray())

fun ByteBuf.writeBytesReversedAdd(src: ByteBuf): ByteBuf {
    for(i in src.writerIndex() - 1 downTo src.readerIndex()) {
        writeByte(src.getByte(i) + HALF_BYTE)
    }
    return this
}

fun ByteBuf.toBitMode() = BitBuf(this)