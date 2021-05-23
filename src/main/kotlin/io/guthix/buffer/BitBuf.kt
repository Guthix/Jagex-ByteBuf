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

/** Wraps this [ByteBuf] into a [BitBuf]. */
public fun ByteBuf.toBitBuf(): BitBuf = BitBuf(this)

/**
 * Wrapper around [ByteBuf] that allows for writing non byte-aligned bits. This implementation maintains the
 * [ByteBuf.readerIndex] and [ByteBuf.writerIndex] of the underlying [ByteBuf]. When writing to the underlying [ByteBuf]
 * however, the [readerIndex] and [writerIndex] will be automatically aligned to the next byte.
 *
 * To create a [BitBuf] use [ByteBuf.toBitBuf] factory method.
 */
public class BitBuf internal constructor(public val buf: ByteBuf) : ReferenceCounted {
    /** The [ByteBuf.capacity] in terms of bits. */
    public val capacity: Long get() = buf.capacity().toLong() * Byte.SIZE_BITS

    /** The maximum [ByteBuf.capacity] in terms of bits. */
    public val maxCapacity: Long get() = buf.maxCapacity().toLong() * Byte.SIZE_BITS

    /**
     * Reader index in terms of bits. Modifying this reader index also modifies the underlying [ByteBuf.readerIndex].
     */
    public var readerIndex: Long = buf.readerIndex().toLong() * Byte.SIZE_BITS
        get() {
            if (buf.readerIndex() != ceil(field / Byte.SIZE_BITS.toDouble()).toInt()) {
                field = buf.readerIndex().toLong() * Byte.SIZE_BITS
            }
            return field
        }
        set(value) {
            field = value
            buf.readerIndex(ceil(field / Byte.SIZE_BITS.toDouble()).toInt())
        }

    /** Returns the number of readable bits. */
    public fun readableBits(): Long = writerIndex - readerIndex

    /**
     * Writer index in terms of bits. Modifying this writer index also modifies the underlying [ByteBuf.writerIndex].
     */
    public var writerIndex: Long = buf.writerIndex().toLong() * Byte.SIZE_BITS
        get() {
            if (buf.writerIndex() != ceil(field / Byte.SIZE_BITS.toDouble()).toInt()) {
                field = buf.writerIndex().toLong() * Byte.SIZE_BITS
            }
            return field
        }
         set(value) {
            field = value
            buf.writerIndex(ceil(field.toDouble() / Byte.SIZE_BITS).toInt())
        }

    /** Returns the number of writable bits. */
    public fun writableBits(): Long = capacity - writerIndex

    /**
     * Gets a [Boolean] at the specified absolute bit [index] in this buffer. This method does not modify [readerIndex]
     * or [writerIndex] of this buffer or the underlying [ByteBuf]. Unlike [ByteBuf.getBoolean], this method only reads
     * from a single bit, instead of a single [Byte].
     *
     * @throws IndexOutOfBoundsException if the [index] is less than 0 or index + 1 is greater than [capacity]
     */
    public fun getBoolean(index: Long): Boolean = getUnsignedBits(index, 1) == 1u

    /**
     * Gets a [UInt] at the specified absolute bit [index] in this buffer. This method does not modify [readerIndex]
     * or [writerIndex] of this buffer or the underlying [ByteBuf].
     *
     * @throws IllegalArgumentException if the [amount] is less than 0 or greater than [Int.SIZE_BITS]
     * @throws IndexOutOfBoundsException if the [index] is less than 0 or index + 1 is greater than [capacity]
     */
    public fun getUnsignedBits(index: Long, amount: Int): UInt {
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
            value = (value shl bitsToGet) or ((buf.getUnsignedByte(byteIndex).toUInt() shr shift) and mask)
            remBits -= bitsToGet
            relBitIndex = 0
            byteIndex++
        }
        return value
    }

    /**
     * Sets the [value] at the specified absolute bit [index] in this buffer. This method does not modify [readerIndex]
     * or [writerIndex] of this buffer or the underlying [ByteBuf]. Unlike [ByteBuf.setBoolean], this method only sets a
     * single bit, instead of a single byte.
     *
     * @throws IndexOutOfBoundsException if the specified [index] is less than 0 or index + 1 is greater than [capacity]
     */
    public fun setBoolean(index: Long, value: Boolean): BitBuf = setBits(index, 1, if (value) 1 else 0)

    /**
     * Sets the [value] at the specified absolute bit [index] in this buffer encoded in [amount] of bits. This method
     * does not modify [readerIndex] or [writerIndex] of this buffer or the underlying [ByteBuf].
     *
     * @throws IllegalArgumentException if the [amount] is less than 0 or greater than [Int.SIZE_BITS]
     * @throws IndexOutOfBoundsException if the specified [index] is less than 0 or index + 1 is greater than [capacity]
     */
    public fun setBits(index: Long, amount: Int, value: Int): BitBuf {
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
            val iValue = (buf.getUnsignedByte(byteIndex).toInt() and (mask shl shift).inv()) or
                    (((value shr (remBits - bitsToSet)) and mask) shl shift)
            buf.setByte(byteIndex, iValue)
            remBits -= bitsToSet
            relBitIndex = 0
            byteIndex++
        }
        return this
    }


    /**
     * Gets a [Boolean] at the current [readerIndex] and increases the [readerIndex] by 1 in this buffer. This method
     * also increases the [ByteBuf.readerIndex] in the underlying [ByteBuf], if required.
     *
     * @throws IndexOutOfBoundsException if [readableBits] is less than 1
     */
    public fun readBoolean(): Boolean = readUnsignedBits(1) == 1u

    /**
     * Gets [amount] of bits as an unsigned value at the current [readerIndex] and increases the [readerIndex] by
     * [amount] in this buffer. This method also increases the [ByteBuf.readerIndex] in the underlying [ByteBuf], if
     * required.
     *
     * @throws IndexOutOfBoundsException if [readableBits] is less than [amount]
     */
    public fun readUnsignedBits(amount: Int): UInt {
        if (readableBits() < amount) throw IndexOutOfBoundsException(
            "readerIndex($readerIndex) + length($amount) exceeds writerIndex($writerIndex): $this"
        )
        val value = getUnsignedBits(readerIndex, amount)
        readerIndex += amount
        return value
    }

    /**
     * Writes a [Boolean] at the current [writerIndex] and increases the [writerIndex] by 1 in this buffer. This method
     * also increases the [ByteBuf.writerIndex] in the underlying [ByteBuf], if required.
     *
     * @throws IndexOutOfBoundsException if [writableBits] is less than 1
     */
    public fun writeBoolean(value: Boolean): BitBuf = writeBits(if (value) 1 else 0, 1)

    /**
     * Writes a [value] in [amount] of bits at the current [writerIndex] and increases the [writerIndex] by [amount] in
     * this buffer. This method also increases the [ByteBuf.writerIndex] in the underlying [ByteBuf], if required.
     *
     * @throws IndexOutOfBoundsException if [writableBits] is less than [amount]
     */
    public fun writeBits(value: Int, amount: Int): BitBuf {
        if (writableBits() < amount) throw IndexOutOfBoundsException(
            "writerIndex($writerIndex) + minWritableBits($amount) exceeds maxCapacity($maxCapacity): $this"
        )
        setBits(writerIndex, amount, value)
        writerIndex += amount
        return this
    }

    override fun refCnt(): Int = buf.refCnt()

    override fun retain(): ReferenceCounted = buf.retain()

    override fun retain(increment: Int): ReferenceCounted = buf.retain()

    override fun touch(): ReferenceCounted = buf.touch()

    override fun touch(hint: Any): ReferenceCounted = buf.touch(hint)

    override fun release(): Boolean = buf.release()

    override fun release(decrement: Int): Boolean = buf.release(decrement)
}