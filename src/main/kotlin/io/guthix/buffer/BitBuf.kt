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
package io.guthix.buffer

import io.netty.buffer.ByteBuf
import kotlin.math.pow

class BitBuf(private val byteBuf: ByteBuf) {
    var bitReaderPos = 0

    var bitWriterPos = 0

    fun readBoolean(): Boolean = readBits(1) == 1

    fun readBits(amount: Int): Int {
        require(amount <= Int.SIZE_BITS) {
            "Amount of bits should be smaller than ${Int.SIZE_BITS}."
        }
        var result = 0
        var bitsToRead = if(bitReaderPos != 0) { // read first byte
            val remainingBits = Byte.SIZE_BITS - bitReaderPos
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
            bitReaderPos += bitsRead
            if(bitReaderPos == Byte.SIZE_BITS) {
                bitReaderPos = 0
            }
            amount - bitsRead
        } else amount
        if(bitsToRead == 0) return result
        while(bitsToRead > Byte.SIZE_BITS) {
            result = (result shl Byte.SIZE_BITS) or byteBuf.readUnsignedByte().toInt()
            bitsToRead -= Byte.SIZE_BITS
        }
        bitReaderPos = bitsToRead % Byte.SIZE_BITS
        val lastByte = (byteBuf.readByte().toInt() shr (Byte.SIZE_BITS - bitsToRead)) and MASK[bitsToRead]
        return (result shl bitsToRead) or lastByte
    }

    fun writeBoolean(value: Boolean) = writeBits(if(value) 1 else 0, 1)

    fun writeBits(value: Int, amount: Int): BitBuf {
        require(amount <= Int.SIZE_BITS) {
            "Amount of bits should be smaller than ${Int.SIZE_BITS}."
        }
        require(value < 2.0.pow(amount)) {
            "Amount should be smaller than ${2.0.pow(amount).toInt()} to encode $value."
        }
        println("bitwriterpos $bitWriterPos")
        var bitsToWrite = if (bitWriterPos != 0) { // write first byte
            val remainingBits = Byte.SIZE_BITS - bitWriterPos
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
            bitWriterPos += bitsWritten
            if (bitWriterPos == Byte.SIZE_BITS) {
                bitWriterPos = 0
            }
            amount - bitsWritten
        } else amount
        if (bitsToWrite == 0) return this
        while (bitsToWrite > Byte.SIZE_BITS) { // write next full bytes
            bitsToWrite -= Byte.SIZE_BITS
            byteBuf.writeByte(value shr bitsToWrite)
        }
        byteBuf.writeByte(value shl (Byte.SIZE_BITS - bitsToWrite)) // write last non full byte
        bitWriterPos = bitsToWrite % Byte.SIZE_BITS
        return this
    }

    fun toByteMode() = byteBuf

    companion object {
        private val MASK = intArrayOf(0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff)
    }
}