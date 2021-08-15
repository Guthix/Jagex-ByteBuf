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
import io.netty.util.ReferenceCounted
import java.nio.charset.Charset

@JvmInline
public value class JByteBufImpl(override val byteBuf: ByteBuf) : JByteBuf {
    override var readerIndex: Int
        get() = byteBuf.readerIndex()
        set(value) {
            byteBuf.readerIndex(value)
        }

    override var writerIndex: Int
        get() = byteBuf.writerIndex()
        set(value) {
            byteBuf.writerIndex(value)
        }

    override fun isDirect(): Boolean = byteBuf.isDirect
    override fun isReadOnly(): Boolean = byteBuf.isReadOnly
    override fun asReadOnly(): JByteBuf = JByteBufImpl(byteBuf.asReadOnly())
    override fun isReadable(): Boolean = byteBuf.isReadable
    override fun isReadable(size: Int): Boolean = byteBuf.isReadable(size)
    override fun isWritable(): Boolean = byteBuf.isWritable
    override fun isWritable(size: Int): Boolean = byteBuf.isWritable(size)
    override fun clear(): JByteBuf {
        byteBuf.clear()
        return this
    }
    override fun markReaderIndex(): JByteBuf {
        byteBuf.markReaderIndex()
        return this
    }
    override fun resetReaderIndex(): JByteBuf {
        byteBuf.resetReaderIndex()
        return this
    }
    override fun markWriterIndex(): JByteBuf {
        byteBuf.markWriterIndex()
        return this
    }
    override fun resetWriterIndex(): JByteBuf {
        byteBuf.resetWriterIndex()
        return this
    }

    override fun refCnt(): Int = byteBuf.refCnt()
    override fun retain(): ReferenceCounted = byteBuf.retain()
    override fun retain(increment: Int): ReferenceCounted = byteBuf.retain(increment)
    override fun touch(): ReferenceCounted = byteBuf.touch()
    override fun touch(hint: Any?): ReferenceCounted = byteBuf.touch(hint)
    override fun release(): Boolean = byteBuf.release()
    override fun release(decrement: Int): Boolean = byteBuf.release(decrement)

    override fun getBoolean(index: Int): Boolean = byteBuf.getBoolean(index)
    override fun getChar(index: Int): Char = byteBuf.getChar(index)
    override fun getByte(index: Int): Byte = byteBuf.getByte(index)
    override fun getByteNeg(index: Int): Byte = byteBuf.getByteNeg(index)
    override fun getByteAdd(index: Int): Byte = byteBuf.getByteAdd(index)
    override fun getByteSub(index: Int): Byte = byteBuf.getByteSub(index)
    override fun getUByte(index: Int): UByte = byteBuf.getUnsignedByte(index).toUByte()
    override fun getUByteNeg(index: Int): UByte = byteBuf.getUnsignedByteNeg(index).toUByte()
    override fun getUByteAdd(index: Int): UByte = byteBuf.getUnsignedByteAdd(index).toUByte()
    override fun getUByteSub(index: Int): UByte = byteBuf.getUnsignedByteSub(index).toUByte()
    override fun getShort(index: Int): Short = byteBuf.getShort(index)
    override fun getShortLE(index: Int): Short = byteBuf.getShortLE(index)
    override fun getShortAdd(index: Int): Short = byteBuf.getShortAdd(index)
    override fun getShortLEAdd(index: Int): Short = byteBuf.getShortLEAdd(index)
    override fun getUShort(index: Int): UShort = byteBuf.getUnsignedShort(index).toUShort()
    override fun getUShortLE(index: Int): UShort = byteBuf.getUnsignedShortLE(index).toUShort()
    override fun getUShortAdd(index: Int): UShort = byteBuf.getUnsignedShortAdd(index).toUShort()
    override fun getUShortLEAdd(index: Int): UShort = byteBuf.getUnsignedShortLEAdd(index).toUShort()
    override fun getMedium(index: Int): Int = byteBuf.getMedium(index)
    override fun getMediumLME(index: Int): Int = byteBuf.getMediumLME(index)
    override fun getMediumRME(index: Int): Int = byteBuf.getMediumRME(index)
    override fun getUMedium(index: Int): UInt = byteBuf.getUnsignedMedium(index).toUInt()
    override fun getUMediumLME(index: Int): UInt = byteBuf.getUnsignedMediumLME(index).toUInt()
    override fun getUMediumRME(index: Int): UInt = byteBuf.getUnsignedMediumRME(index).toUInt()
    override fun getInt(index: Int): Int = byteBuf.getInt(index)
    override fun getIntME(index: Int): Int = byteBuf.getIntME(index)
    override fun getIntIME(index: Int): Int = byteBuf.getIntIME(index)
    override fun getIntLE(index: Int): Int = byteBuf.getIntLE(index)
    override fun getUInt(index: Int): UInt = byteBuf.getUnsignedInt(index).toUInt()
    override fun getUIntME(index: Int): UInt = byteBuf.getUnsignedIntME(index).toUInt()
    override fun getUIntIME(index: Int): UInt = byteBuf.getUnsignedIntIME(index).toUInt()
    override fun getUIntLE(index: Int): UInt = byteBuf.getUnsignedIntLE(index).toUInt()
    override fun getSmallLong(index: Int): Long = byteBuf.getSmallLong(index)
    override fun getUSmallLong(index: Int): ULong = byteBuf.getUnsignedSmallLong(index).toULong()
    override fun getLong(index: Int): Long = byteBuf.getLong(index)
    override fun getULong(index: Int): ULong = byteBuf.getLong(index).toULong()
    override fun getBytes(index: Int, dst: ByteArray): JByteBuf {
        byteBuf.getBytes(index, dst)
        return this
    }
    override fun getBytesAdd(index: Int, dst: ByteArray): JByteBuf {
        byteBuf.getBytesAdd(index, dst)
        return this
    }
    override fun getBytesReversed(index: Int, dst: ByteArray): JByteBuf {
        byteBuf.getBytesReversed(index, dst)
        return this
    }
    override fun getBytesReversedAdd(index: Int, dst: ByteArray): JByteBuf {
        byteBuf.getBytesReversedAdd(index, dst)
        return this
    }

    override fun setBoolean(index: Int, value: Boolean): JByteBuf {
        byteBuf.setBoolean(index, value)
        return this
    }
    override fun setChar(index: Int, value: Char): JByteBuf {
        byteBuf.setChar(index, value.code)
        return this
    }
    override fun setByte(index: Int, value: Int): JByteBuf {
        byteBuf.setByte(index, value)
        return this
    }
    override fun setByteNeg(index: Int, value: Int): JByteBuf {
        byteBuf.setByteNeg(index, value)
        return this
    }
    override fun setByteAdd(index: Int, value: Int): JByteBuf {
        byteBuf.setByteAdd(index, value)
        return this
    }
    override fun setByteSub(index: Int, value: Int): JByteBuf {
        byteBuf.setByteSub(index, value)
        return this
    }
    override fun setShort(index: Int, value: Int): JByteBuf {
        byteBuf.setShort(index, value)
        return this
    }
    override fun setShortLE(index: Int, value: Int): JByteBuf {
        byteBuf.setShortLE(index, value)
        return this
    }
    override fun setShortAdd(index: Int, value: Int): JByteBuf {
        byteBuf.setShortAdd(index, value)
        return this
    }
    override fun setShortLEAdd(index: Int, value: Int): JByteBuf {
        byteBuf.setShortLEAdd(index, value)
        return this
    }
    override fun setMedium(index: Int, value: Int): JByteBuf {
        byteBuf.setMedium(index, value)
        return this
    }
    override fun setMediumLME(index: Int, value: Int): JByteBuf {
        byteBuf.setMediumLME(index, value)
        return this
    }
    override fun setMediumRME(index: Int, value: Int): JByteBuf {
        byteBuf.setMediumRME(index, value)
        return this
    }
    override fun setInt(index: Int, value: Int): JByteBuf {
        byteBuf.setInt(index, value)
        return this
    }
    override fun setIntME(index: Int, value: Int): JByteBuf {
        byteBuf.setIntME(index, value)
        return this
    }
    override fun setIntIME(index: Int, value: Int): JByteBuf {
        byteBuf.setIntIME(index, value)
        return this
    }
    override fun setIntLE(index: Int, value: Int): JByteBuf {
        byteBuf.setIntLE(index, value)
        return this
    }
    override fun setSmallLong(index: Int, value: Long): JByteBuf {
        byteBuf.setSmallLong(index, value)
        return this
    }
    override fun setLong(index: Int, value: Long): JByteBuf {
        byteBuf.setLong(index, value)
        return this
    }
    override fun setBytes(index: Int, value: ByteArray): JByteBuf {
        byteBuf.setBytes(index, value)
        return this
    }
    override fun setBytes(index: Int, value: JByteBuf): JByteBuf {
        byteBuf.setBytes(index, value.byteBuf)
        return this
    }
    override fun setBytesAdd(index: Int, value: ByteArray): JByteBuf {
        byteBuf.setBytesAdd(index,  value)
        return this
    }
    override fun setBytesAdd(index: Int, value: JByteBuf): JByteBuf {
        byteBuf.setBytesAdd(index,  value.byteBuf)
        return this
    }
    override fun setBytesReversed(index: Int, value: ByteArray): JByteBuf {
        byteBuf.setBytesReversed(index,  value)
        return this
    }
    override fun setBytesReversed(index: Int, value: JByteBuf): JByteBuf {
        byteBuf.setBytesReversed(index,  value.byteBuf)
        return this
    }
    override fun setBytesReversedAdd(index: Int, value: ByteArray): JByteBuf {
        byteBuf.setBytesReversedAdd(index,  value)
        return this
    }
    override fun setBytesReversedAdd(index: Int, value: JByteBuf): JByteBuf {
        byteBuf.setBytesReversedAdd(index,  value.byteBuf)
        return this
    }

    override fun readBoolean(): Boolean = byteBuf.readBoolean()
    override fun readChar(): Char = byteBuf.readChar()
    override fun readByte(): Byte = byteBuf.readByte()
    override fun readByteNeg(): Byte = byteBuf.readByteNeg()
    override fun readByteAdd(): Byte = byteBuf.readByteAdd()
    override fun readByteSub(): Byte = byteBuf.readByteSub()
    override fun readUByte(): UByte = byteBuf.readUnsignedByte().toUByte()
    override fun readUByteNeg(): UByte = byteBuf.readUnsignedByteNeg().toUByte()
    override fun readUByteAdd(): UByte = byteBuf.readUnsignedByteAdd().toUByte()
    override fun readUByteSub(): UByte = byteBuf.readUnsignedByteSub().toUByte()
    override fun readShort(): Short = byteBuf.readShort()
    override fun readShortLE(): Short = byteBuf.readShortLE()
    override fun readShortAdd(): Short = byteBuf.readShortAdd()
    override fun readShortLEAdd(): Short = byteBuf.readShortLEAdd()
    override fun readUShort(): UShort = byteBuf.readUnsignedShort().toUShort()
    override fun readUShortLE(): UShort = byteBuf.readUnsignedShortLE().toUShort()
    override fun readUShortAdd(): UShort = byteBuf.readUnsignedShortAdd().toUShort()
    override fun readUShortLEAdd(): UShort = byteBuf.readUnsignedShortLEAdd().toUShort()
    override fun readMedium(): Int = byteBuf.readMedium()
    override fun readMediumLME(): Int = byteBuf.readMediumLME()
    override fun readMediumRME(): Int = byteBuf.readMediumRME()
    override fun readUMedium(): UInt = byteBuf.readUnsignedMedium().toUInt()
    override fun readUMediumLME(): UInt = byteBuf.readUnsignedMediumLME().toUInt()
    override fun readUMediumRME(): UInt = byteBuf.readUnsignedMediumRME().toUInt()
    override fun readInt(): Int = byteBuf.readInt()
    override fun readIntME(): Int = byteBuf.readIntME()
    override fun readIntIME(): Int = byteBuf.readIntIME()
    override fun readIntLE(): Int = byteBuf.readIntLE()
    override fun readUInt(): UInt = byteBuf.readUnsignedInt().toUInt()
    override fun readUIntME(): UInt = byteBuf.readUnsignedIntME().toUInt()
    override fun readUIntIME(): UInt = byteBuf.readUnsignedIntIME().toUInt()
    override fun readUIntLE(): UInt = byteBuf.readUnsignedIntLE().toUInt()
    override fun readSmallLong(): Long = byteBuf.readSmallLong()
    override fun readUSmallLong(): ULong = byteBuf.readUnsignedSmallLong().toULong()
    override fun readLong(): Long = byteBuf.readLong()
    override fun readULong(): ULong = byteBuf.readLong().toULong()
    override fun readShortSmart(): Short = byteBuf.readShortSmart()
    override fun readUShortSmart(): UShort = byteBuf.readUnsignedShortSmart().toUShort()
    override fun readIncrShortSmart(): Int = byteBuf.readIncrShortSmart()
    override fun readIntSmart(): Int = byteBuf.readIntSmart()
    override fun readUIntSmart(): UInt = byteBuf.readUnsignedIntSmart().toUInt()
    override fun readNullableIntSmart(): UInt? = byteBuf.readNullableUnsignedIntSmart()?.toUInt()
    override fun readVarInt(): Int = byteBuf.readVarInt()
    override fun readString(charset: Charset): String = byteBuf.readString(charset)
    override fun readVersionedString(charset: Charset, expectedVersion: Int): String =
        byteBuf.readVersionedString(charset, expectedVersion)
    override fun readBytes(dst: ByteArray): JByteBuf {
        byteBuf.readBytes(dst)
        return this
    }
    override fun readBytesAdd(dst: ByteArray): JByteBuf {
        byteBuf.readBytesAdd(dst)
        return this
    }
    override fun readBytesReversed(dst: ByteArray): JByteBuf {
        byteBuf.readBytesReversed(dst)
        return this
    }
    override fun readBytesReversedAdd(dst: ByteArray): JByteBuf {
        byteBuf.readBytesReversedAdd(dst)
        return this
    }

    override fun writeBoolean(value: Boolean): JByteBuf {
        byteBuf.writeBoolean(value)
        return this
    }
    override fun writeChar(value: Char): JByteBuf {
        byteBuf.writeChar(value.code)
        return this
    }
    override fun writeByte(value: Int): JByteBuf {
        byteBuf.writeByte(value)
        return this
    }
    override fun writeByteNeg(value: Int): JByteBuf {
        byteBuf.writeByteNeg(value)
        return this
    }
    override fun writeByteAdd(value: Int): JByteBuf {
        byteBuf.writeByteAdd(value)
        return this
    }
    override fun writeByteSub(value: Int): JByteBuf {
        byteBuf.writeByteSub(value)
        return this
    }
    override fun writeShort(value: Int): JByteBuf {
        byteBuf.writeShort(value)
        return this
    }
    override fun writeShortLE(value: Int): JByteBuf {
        byteBuf.writeShortLE(value)
        return this
    }
    override fun writeShortAdd(value: Int): JByteBuf {
        byteBuf.writeShortAdd(value)
        return this
    }
    override fun writeShortLEAdd(value: Int): JByteBuf {
        byteBuf.writeShortLEAdd(value)
        return this
    }
    override fun writeMedium(value: Int): JByteBuf {
        byteBuf.writeMedium(value)
        return this
    }
    override fun writeMediumLME(value: Int): JByteBuf {
        byteBuf.writeMediumLME(value)
        return this
    }
    override fun writeMediumRME(value: Int): JByteBuf {
        byteBuf.writeMediumRME(value)
        return this
    }
    override fun writeInt(value: Int): JByteBuf {
        byteBuf.writeInt(value)
        return this
    }
    override fun writeIntME(value: Int): JByteBuf {
        byteBuf.writeIntME(value)
        return this
    }
    override fun writeIntIME(value: Int): JByteBuf {
        byteBuf.writeIntIME(value)
        return this
    }
    override fun writeIntLE(value: Int): JByteBuf {
        byteBuf.writeIntLE(value)
        return this
    }
    override fun writeSmallLong(value: Long): JByteBuf {
        byteBuf.writeSmallLong(value)
        return this
    }
    override fun writeLong(value: Long): JByteBuf {
        byteBuf.writeLong(value)
        return this
    }
    override fun writeShortSmart(value: Int): JByteBuf {
        byteBuf.writeShortSmart(value)
        return this
    }
    override fun writeUShortSmart(value: Int): JByteBuf {
        byteBuf.writeUnsignedShortSmart(value)
        return this
    }
    override fun writeIncrShortSmart(value: Int): JByteBuf {
        byteBuf.writeIncrShortSmart(value)
        return this
    }
    override fun writeIntSmart(value: Int): JByteBuf {
        byteBuf.writeIntSmart(value)
        return this
    }
    override fun writeUIntSmart(value: Int): JByteBuf {
        byteBuf.writeUnsignedIntSmart(value)
        return this
    }
    override fun writeNullableIntSmart(value: Int?): JByteBuf {
        byteBuf.writeNullableUnsignedIntSmart(value)
        return this
    }
    override fun writeVarInt(value: Int): JByteBuf {
        byteBuf.writeVarInt(value)
        return this
    }
    override fun writeString(value: String, charset: Charset): JByteBuf {
        byteBuf.writeString(value, charset)
        return this
    }
    override fun writeString(value: String, charset: Charset, version: Int): JByteBuf {
        byteBuf.writeVersionedString(value, charset, version)
        return this
    }
    override fun writeBytes(value: ByteArray): JByteBuf {
        byteBuf.writeBytes(value)
        return this
    }
    override fun writeBytes(value: JByteBuf): JByteBuf {
        byteBuf.writeBytes(value.byteBuf)
        return this
    }
    override fun writeBytesAdd(value: ByteArray): JByteBuf {
        byteBuf.writeBytesAdd(value)
        return this
    }
    override fun writeBytesAdd(value: JByteBuf): JByteBuf {
        byteBuf.writeBytesAdd(value.byteBuf)
        return this
    }
    override fun writeBytesReversed(value: ByteArray): JByteBuf {
        byteBuf.writeBytesReversed(value)
        return this
    }
    override fun writeBytesReversed(value: JByteBuf): JByteBuf {
        byteBuf.writeBytesReversed(value.byteBuf)
        return this
    }
    override fun writeBytesReversedAdd(value: ByteArray): JByteBuf {
        byteBuf.writeBytesReversedAdd(value)
        return this
    }
    override fun writeBytesReversedAdd(value: JByteBuf): JByteBuf {
        byteBuf.writeBytesReversedAdd(value.byteBuf)
        return this
    }
}