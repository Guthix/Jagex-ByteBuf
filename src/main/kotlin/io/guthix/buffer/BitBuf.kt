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
import kotlin.math.pow

public class BitBuf(private val byteBuf: ByteBuf) {
    public var relativeBitReaderIndex: Int = 0

    public var relativeBitWriterIndex: Int = 0

    public fun readBoolean(): Boolean = readUnsignedBits(1) == 1

    public fun readUnsignedBits(amount: Int): Int {
        require(amount > 0 && amount <= Int.SIZE_BITS) { "Invalid read amount: $amount." }
        var result = 0
        var bitsToRead = if(relativeBitReaderIndex != 0) { // read first byte
            val remainingBits = Byte.SIZE_BITS - relativeBitReaderIndex
            val curByteIndex = byteBuf.readerIndex() - 1
            val curByte = byteBuf.getByte(curByteIndex).toInt()
            val readableCurByte = curByte and MASK[remainingBits] // remove non-readable bits
            val shiftAmount = amount - remainingBits
            val bitsRead = if(shiftAmount >= 0) {
                result = result or readableCurByte
                remainingBits
            } else {
                result = result or (readableCurByte shr -shiftAmount)
                amount
            }
            relativeBitReaderIndex += bitsRead
            if(relativeBitReaderIndex == Byte.SIZE_BITS) {
                relativeBitReaderIndex = 0
            }
            amount - bitsRead
        } else amount
        if(bitsToRead == 0) return result
        while(bitsToRead > Byte.SIZE_BITS) {
            result = (result shl Byte.SIZE_BITS) or byteBuf.readUnsignedByte().toInt()
            bitsToRead -= Byte.SIZE_BITS
        }
        relativeBitReaderIndex = bitsToRead % Byte.SIZE_BITS
        val lastByte = (byteBuf.readByte().toInt() shr (Byte.SIZE_BITS - bitsToRead)) and MASK[bitsToRead]
        return (result shl bitsToRead) or lastByte
    }

    public fun writeBoolean(value: Boolean): BitBuf = writeBits(if(value) 1 else 0, 1)

    public fun writeBits(value: Int, amount: Int): BitBuf {
        require(amount > 0 && amount <= Int.SIZE_BITS) { "Invalid write amount: $amount." }
        require(value < 2.0.pow(amount)) {
            "Amount should be smaller than ${2.0.pow(amount).toInt()} to encode $value."
        }
        var bitsToWrite = if (relativeBitWriterIndex != 0) { // write first byte
            val remainingBits = Byte.SIZE_BITS - relativeBitWriterIndex
            val curByteIndex = byteBuf.writerIndex() - 1
            val curByte = byteBuf.getByte(curByteIndex).toInt()
            val shiftAmount = amount - remainingBits
            val bitsWritten = if (shiftAmount >= 0) {
                byteBuf.setByte(curByteIndex, curByte or (value shr shiftAmount))
                remainingBits
            } else {
                byteBuf.setByte(curByteIndex, curByte or (value shl -shiftAmount))
                amount
            }
            relativeBitWriterIndex += bitsWritten
            if (relativeBitWriterIndex == Byte.SIZE_BITS) {
                relativeBitWriterIndex = 0
            }
            amount - bitsWritten
        } else amount
        if (bitsToWrite == 0) return this
        while (bitsToWrite > Byte.SIZE_BITS) { // write next full bytes
            bitsToWrite -= Byte.SIZE_BITS
            byteBuf.writeByte(value shr bitsToWrite)
        }
        byteBuf.writeByte(value shl (Byte.SIZE_BITS - bitsToWrite)) // write last non full byte
        relativeBitWriterIndex = bitsToWrite % Byte.SIZE_BITS
        return this
    }

    public fun toByteMode(): ByteBuf = byteBuf

    public fun release(): Boolean = byteBuf.release()

    public fun release(decrement: Int): Boolean = byteBuf.release(decrement)

    public companion object {
        private val MASK = intArrayOf(0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff)
    }
}