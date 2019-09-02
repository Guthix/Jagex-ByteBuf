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



fun ByteBuf.setByteNEG(index: Int, value: Int) = setByte(index, -value)

fun ByteBuf.setByteADD(index: Int, value: Int) = setByte(index, value + HALF_BYTE)

fun ByteBuf.seteByteSUB(index: Int, value: Int) = setByte(index, HALF_BYTE - value)



fun ByteBuf.readByteNEG() = (-readByte()).toByte()

fun ByteBuf.readByteADD() = (readByte() - HALF_BYTE).toByte()

fun ByteBuf.readByteSUB() = (HALF_BYTE - readByte()).toByte()

fun ByteBuf.readUnsignedByteNEG() = (-readUnsignedByte()).toShort()

fun ByteBuf.readUnsignedByteADD() = (readUnsignedByte() - HALF_BYTE).toShort()

fun ByteBuf.readUnsignedByteSUB() = (HALF_BYTE - readUnsignedByte()).toShort()



fun ByteBuf.writeByteNEG(value: Int) = writeByte(-value)

fun ByteBuf.writeByteADD(value: Int) = writeByte(value + HALF_BYTE)

fun ByteBuf.writeByteSUB(value: Int) = writeByte(HALF_BYTE - value)
