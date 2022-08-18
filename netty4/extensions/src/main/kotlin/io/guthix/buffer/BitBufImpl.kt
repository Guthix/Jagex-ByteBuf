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
import kotlin.math.ceil
import kotlin.math.min

public class BitBufImpl internal constructor(public override val byteBuf: ByteBuf) : BitBuf {
    public override val capacity: Long get() = byteBuf.capacity().toLong() * Byte.SIZE_BITS

    public override val maxCapacity: Long get() = byteBuf.maxCapacity().toLong() * Byte.SIZE_BITS

    public override var readerIndex: Long = byteBuf.readerIndex().toLong() * Byte.SIZE_BITS
        get() {
            if (byteBuf.readerIndex() != ceil(field / Byte.SIZE_BITS.toDouble()).toInt()) {
                field = byteBuf.readerIndex().toLong() * Byte.SIZE_BITS
            }
            return field
        }
        set(value) {
            field = value
            byteBuf.readerIndex(ceil(field / Byte.SIZE_BITS.toDouble()).toInt())
        }

    public override fun readableBits(): Long = writerIndex - readerIndex

    public override var writerIndex: Long = byteBuf.writerIndex().toLong() * Byte.SIZE_BITS
        get() {
            if (byteBuf.writerIndex() != ceil(field / Byte.SIZE_BITS.toDouble()).toInt()) {
                field = byteBuf.writerIndex().toLong() * Byte.SIZE_BITS
            }
            return field
        }
        set(value) {
            field = value
            byteBuf.writerIndex(ceil(field.toDouble() / Byte.SIZE_BITS).toInt())
        }

    public override fun writableBits(): Long = capacity - writerIndex

    public override fun getBoolean(index: Long): Boolean = getUnsignedBits(index, 1) == 1u

    public override fun getUnsignedBits(index: Long, amount: Int): UInt {
        require(amount > 0 && amount <= Int.SIZE_BITS) { "Amount should be between 0 and ${Int.SIZE_BITS} bits." }
        if (index < 0 || (index + amount) > capacity) throw IndexOutOfBoundsException(
            "index: $index, length: $capacity (expected: range(0, $capacity))"
        )
        var byteIndex = (index / Byte.SIZE_BITS).toInt()
        var relBitIndex = index.toInt() and (Byte.SIZE_BITS - 1)
        var value = 0u
        var remBits = amount
        while (remBits > 0) {
            val bitsToGet = min(Byte.SIZE_BITS - relBitIndex, remBits)
            val shift = (Byte.SIZE_BITS - (relBitIndex + bitsToGet)) and (Byte.SIZE_BITS - 1)
            val mask = (1u shl bitsToGet) - 1u
            value = (value shl bitsToGet) or ((byteBuf.getUnsignedByte(byteIndex).toUInt() shr shift) and mask)
            remBits -= bitsToGet
            relBitIndex = 0
            byteIndex++
        }
        return value
    }

    public override fun setBoolean(index: Long, value: Boolean): BitBuf = setBits(index, 1, if (value) 1 else 0)

    public override fun setBits(index: Long, amount: Int, value: Int): BitBuf {
        require(amount > 0 && amount <= Int.SIZE_BITS) { "Amount should be between 0 and ${Int.SIZE_BITS} bits." }
        if (index < 0 || (index + amount) > capacity) throw IndexOutOfBoundsException(
            "index: $index, length: $capacity (expected: range(0, $capacity))"
        )
        var byteIndex = (index / Byte.SIZE_BITS).toInt()
        var relBitIndex = index.toInt() and (Byte.SIZE_BITS - 1)
        var remBits = amount
        while (remBits > 0) {
            val bitsToSet = min(Byte.SIZE_BITS - relBitIndex, remBits)
            val shift = (Byte.SIZE_BITS - (relBitIndex + bitsToSet)) and (Byte.SIZE_BITS - 1)
            val mask = (1 shl bitsToSet) - 1
            val iValue = (byteBuf.getUnsignedByte(byteIndex).toInt() and (mask shl shift).inv()) or
                (((value shr (remBits - bitsToSet)) and mask) shl shift)
            byteBuf.setByte(byteIndex, iValue)
            remBits -= bitsToSet
            relBitIndex = 0
            byteIndex++
        }
        return this
    }

    public override fun readBoolean(): Boolean = readUnsignedBits(1) == 1u

    public override fun readUnsignedBits(amount: Int): UInt {
        if (readableBits() < amount) throw IndexOutOfBoundsException(
            "readerIndex($readerIndex) + length($amount) exceeds writerIndex($writerIndex): $this"
        )
        val value = getUnsignedBits(readerIndex, amount)
        readerIndex += amount
        return value
    }

    public override fun writeBoolean(value: Boolean): BitBuf = writeBits(if (value) 1 else 0, 1)

    public override fun writeBits(value: Int, amount: Int): BitBuf {
        if (writableBits() < amount) throw IndexOutOfBoundsException(
            "writerIndex($writerIndex) + minWritableBits($amount) exceeds maxCapacity($maxCapacity): $this"
        )
        setBits(writerIndex, amount, value)
        writerIndex += amount
        return this
    }

    override fun refCnt(): Int = byteBuf.refCnt()

    override fun retain(): ReferenceCounted = byteBuf.retain()

    override fun retain(increment: Int): ReferenceCounted = byteBuf.retain()

    override fun touch(): ReferenceCounted = byteBuf.touch()

    override fun touch(hint: Any): ReferenceCounted = byteBuf.touch(hint)

    override fun release(): Boolean = byteBuf.release()

    override fun release(decrement: Int): Boolean = byteBuf.release(decrement)
}