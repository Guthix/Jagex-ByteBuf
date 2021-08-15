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

/** Wraps this [ByteBuf] into a [JByteBuf]. */
public fun ByteBuf.toJBytebuf(): JByteBuf = JByteBufImpl(this)

/**
 * A wrapper around [byteBuf] that supports all extensions and unsigned types.
 */
public interface JByteBuf : ReferenceCounted {
    public val byteBuf: ByteBuf

    public var readerIndex: Int
    public var writerIndex: Int

    public fun isDirect(): Boolean
    public fun isReadOnly(): Boolean
    public fun asReadOnly(): JByteBuf
    public fun isReadable(): Boolean
    public fun isReadable(size: Int): Boolean
    public fun isWritable(): Boolean
    public fun isWritable(size: Int): Boolean
    public fun clear(): JByteBuf
    public fun markReaderIndex(): JByteBuf
    public fun resetReaderIndex(): JByteBuf
    public fun markWriterIndex(): JByteBuf
    public fun resetWriterIndex(): JByteBuf

    public fun getByte(index: Int): Byte
    public fun getByteNeg(index: Int): Byte
    public fun getByteAdd(index: Int): Byte
    public fun getByteSub(index: Int): Byte
    public fun getUByte(index: Int): UByte
    public fun getUByteNeg(index: Int): UByte
    public fun getUByteAdd(index: Int): UByte
    public fun getUByteSub(index: Int): UByte
    public fun getShort(index: Int): Short
    public fun getShortLE(index: Int): Short
    public fun getShortAdd(index: Int): Short
    public fun getShortLEAdd(index: Int): Short
    public fun getUShort(index: Int): UShort
    public fun getUShortLE(index: Int): UShort
    public fun getUShortAdd(index: Int): UShort
    public fun getUShortLEAdd(index: Int): UShort
    public fun getMedium(index: Int): Int
    public fun getMediumLME(index: Int): Int
    public fun getMediumRME(index: Int): Int
    public fun getUMedium(index: Int): UInt
    public fun getUMediumLME(index: Int): UInt
    public fun getUMediumRME(index: Int): UInt
    public fun getInt(index: Int): Int
    public fun getIntME(index: Int): Int
    public fun getIntIME(index: Int): Int
    public fun getIntLE(index: Int): Int
    public fun getUInt(index: Int): UInt
    public fun getUIntME(index: Int): UInt
    public fun getUIntIME(index: Int): UInt
    public fun getUIntLE(index: Int): UInt
    public fun getSmallLong(index: Int): Long
    public fun getUSmallLong(index: Int): ULong
    public fun getLong(index: Int): Long
    public fun getULong(index: Int): ULong
    public fun getBytes(index: Int, dst: ByteArray): JByteBuf
    public fun getBytesAdd(index: Int, dst: ByteArray): JByteBuf
    public fun getBytesReversed(index: Int, dst: ByteArray): JByteBuf
    public fun getBytesReversedAdd(index: Int, dst: ByteArray): JByteBuf
    
    public fun setByte(index: Int, value: Int): JByteBuf
    public fun setByteNeg(index: Int, value: Int): JByteBuf
    public fun setByteAdd(index: Int, value: Int): JByteBuf
    public fun setByteSub(index: Int, value: Int): JByteBuf
    public fun setShort(index: Int, value: Int): JByteBuf
    public fun setShortLE(index: Int, value: Int): JByteBuf
    public fun setShortAdd(index: Int, value: Int): JByteBuf
    public fun setShortLEAdd(index: Int, value: Int): JByteBuf
    public fun setMedium(index: Int, value: Int): JByteBuf
    public fun setMediumLME(index: Int, value: Int): JByteBuf
    public fun setMediumRME(index: Int, value: Int): JByteBuf
    public fun setInt(index: Int, value: Int): JByteBuf
    public fun setIntME(index: Int, value: Int): JByteBuf
    public fun setIntIME(index: Int, value: Int): JByteBuf
    public fun setIntLE(index: Int, value: Int): JByteBuf
    public fun setSmallLong(index: Int, value: Long): JByteBuf
    public fun setLong(index: Int, value: Long): JByteBuf
    public fun setBytes(index: Int, value: ByteArray): JByteBuf
    public fun setBytes(index: Int, value: JByteBuf): JByteBuf
    public fun setBytesAdd(index: Int, value: ByteArray): JByteBuf
    public fun setBytesAdd(index: Int, value: JByteBuf): JByteBuf
    public fun setBytesReversed(index: Int, value: ByteArray): JByteBuf
    public fun setBytesReversed(index: Int, value: JByteBuf): JByteBuf
    public fun setBytesReversedAdd(index: Int, value: ByteArray): JByteBuf
    public fun setBytesReversedAdd(index: Int, value: JByteBuf): JByteBuf
    
    public fun readByte(): Byte
    public fun readByteNeg(): Byte
    public fun readByteAdd(): Byte
    public fun readByteSub(): Byte
    public fun readUByte(): UByte
    public fun readUByteNeg(): UByte
    public fun readUByteAdd(): UByte
    public fun readUByteSub(): UByte
    public fun readShort(): Short
    public fun readShortLE(): Short
    public fun readShortAdd(): Short
    public fun readShortLEAdd(): Short
    public fun readUShort(): UShort
    public fun readUShortLE(): UShort
    public fun readUShortAdd(): UShort
    public fun readUShortLEAdd(): UShort
    public fun readMedium(): Int
    public fun readMediumLME(): Int
    public fun readMediumRME(): Int
    public fun readUMedium(): UInt
    public fun readUMediumLME(): UInt
    public fun readUMediumRME(): UInt
    public fun readInt(): Int
    public fun readIntME(): Int
    public fun readIntIME(): Int
    public fun readIntLE(): Int
    public fun readUInt(): UInt
    public fun readUIntME(): UInt
    public fun readUIntIME(): UInt
    public fun readUIntLE(): UInt
    public fun readSmallLong(): Long
    public fun readUSmallLong(): ULong
    public fun readLong(): Long
    public fun readULong(): ULong
    public fun readShortSmart(): Short
    public fun readUShortSmart(): UShort
    public fun readIncrShortSmart(): Int
    public fun readIntSmart(): Int
    public fun readUIntSmart(): UInt
    public fun readNullableIntSmart(): UInt?
    public fun readVarInt(): Int
    public fun readBytes(dst: ByteArray): JByteBuf
    public fun readBytesAdd(dst: ByteArray): JByteBuf
    public fun readBytesReversed(dst: ByteArray): JByteBuf
    public fun readBytesReversedAdd(dst: ByteArray): JByteBuf

    public fun writeByte(value: Int): JByteBuf
    public fun writeByteNeg(value: Int): JByteBuf
    public fun writeByteAdd(value: Int): JByteBuf
    public fun writeByteSub(value: Int): JByteBuf
    public fun writeShort(value: Int): JByteBuf
    public fun writeShortLE(value: Int): JByteBuf
    public fun writeShortAdd(value: Int): JByteBuf
    public fun writeShortLEAdd(value: Int): JByteBuf
    public fun writeMedium(value: Int): JByteBuf
    public fun writeMediumLME(value: Int): JByteBuf
    public fun writeMediumRME(value: Int): JByteBuf
    public fun writeInt(value: Int): JByteBuf
    public fun writeIntME(value: Int): JByteBuf
    public fun writeIntIME(value: Int): JByteBuf
    public fun writeIntLE(value: Int): JByteBuf
    public fun writeSmallLong(value: Long): JByteBuf
    public fun writeLong(value: Long): JByteBuf
    public fun writeShortSmart(value: Int): JByteBuf
    public fun writeUShortSmart(value: Int): JByteBuf
    public fun writeIncrShortSmart(value: Int): JByteBuf
    public fun writeIntSmart(value: Int): JByteBuf
    public fun writeUIntSmart(value: Int): JByteBuf
    public fun writeNullableIntSmart(value: Int?): JByteBuf
    public fun writeVarInt(value: Int): JByteBuf
    public fun writeBytes(value: ByteArray): JByteBuf
    public fun writeBytes(value: JByteBuf): JByteBuf
    public fun writeBytesAdd(value: ByteArray): JByteBuf
    public fun writeBytesAdd(value: JByteBuf): JByteBuf
    public fun writeBytesReversed(value: ByteArray): JByteBuf
    public fun writeBytesReversed(value: JByteBuf): JByteBuf
    public fun writeBytesReversedAdd(value: ByteArray): JByteBuf
    public fun writeBytesReversedAdd(value: JByteBuf): JByteBuf
}