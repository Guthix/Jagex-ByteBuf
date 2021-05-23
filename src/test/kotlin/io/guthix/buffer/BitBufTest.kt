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

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.RandomSource
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlin.math.ceil
import kotlin.math.pow

private data class BitBufTestCase(val amount: Int, val value: UInt)

private fun bitBufArbGen(rs: RandomSource): BitBufTestCase {
    val amount = Arb.int(1..Int.SIZE_BITS).next(rs)
    val value = Arb.long(0, 2.0.pow(amount.toDouble()).toLong() - 1).next(rs).toUInt()
    return BitBufTestCase(amount, value)
}

private val bitBufArb = arbitrary { bitBufArbGen(it) }

private fun bitBufArrayArb(max: Int) = arbitrary { rs -> Array(Arb.int(1..max).next(rs)) { bitBufArbGen(rs) } }

class BitBufTest : StringSpec({
    "BitBuf read/write test" {
        checkAll(100_000, bitBufArb) { (amount, value) ->
            val bitBuf = ByteBufAllocator.DEFAULT.buffer(ceil(amount.toDouble() / Byte.SIZE_BITS).toInt()).toBitBuf()
            try {
                bitBuf.writeBits(value.toInt(), amount)
                bitBuf.readUnsignedBits(amount) shouldBe value
            } finally {
                bitBuf.release()
            }
        }
    }

    "BitBuf sequential read/write test" {
        checkAll(50_000, bitBufArrayArb(50)) { testCases ->
            val bitAmount = testCases.sumOf { it.amount }
            val bitBuf = ByteBufAllocator.DEFAULT.buffer(ceil(bitAmount.toDouble() / Byte.SIZE_BITS).toInt()).toBitBuf()
            try {
                for (testCase in testCases) {
                    bitBuf.writeBits(testCase.value.toInt(), testCase.amount)
                }
                for (testCase in testCases) bitBuf.readUnsignedBits(testCase.amount) shouldBe testCase.value
            } finally {
                bitBuf.release()
            }
        }
    }

    "Mixed BitBuf/ByteBuf sequential read/write test" {
        checkAll(50_000, bitBufArrayArb(50)) { testCases ->
            val maxByteAmount = testCases.sumOf { ceil(it.amount / Byte.SIZE_BITS.toDouble()) }.toInt()
            val byteBuf = ByteBufAllocator.DEFAULT.buffer(maxByteAmount)
            val bitBuf = byteBuf.toBitBuf()
            try {
                for (testCase in testCases) {
                    when (testCase.amount) {
                        Byte.SIZE_BITS -> byteBuf.writeByte(testCase.value.toInt())
                        Short.SIZE_BITS -> byteBuf.writeShort(testCase.value.toInt())
                        Int.SIZE_BITS -> byteBuf.writeInt(testCase.value.toInt())
                        else -> bitBuf.writeBits(testCase.value.toInt(), testCase.amount)
                    }
                }
                for (testCase in testCases) {
                    when (testCase.amount) {
                        Byte.SIZE_BITS -> byteBuf.readUnsignedByte().toUInt() shouldBe testCase.value
                        Short.SIZE_BITS -> byteBuf.readUnsignedShort().toUInt() shouldBe testCase.value
                        Int.SIZE_BITS -> byteBuf.readUnsignedInt().toUInt() shouldBe testCase.value
                        else -> bitBuf.readUnsignedBits(testCase.amount) shouldBe testCase.value
                    }
                }
            } finally {
                bitBuf.release()
            }
        }
    }
})