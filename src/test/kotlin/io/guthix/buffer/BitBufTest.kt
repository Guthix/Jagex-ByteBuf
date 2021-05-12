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
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlin.math.pow

private data class BitBufTestCase(val amount: Int, val value: Int)

private val bitBufArb = arbitrary { rs ->
    val amount = Arb.int(1..32).next(rs)
    val pOf2 = 2.0.pow(amount.toDouble()).toInt()
    val value = Arb.int(0, pOf2 - 1).next(rs)
    BitBufTestCase(amount, value)
}

class BitBufTest : StringSpec({
    "BitBuf read/write test" {
        checkAll(bitBufArb) { (amount, value) ->
            val bitBuf = ByteBufAllocator.DEFAULT.buffer(amount / Int.SIZE_BITS + 1).toBitMode()
            try {
                bitBuf.writeBits(value, amount)
                bitBuf.relativeBitWriterIndex shouldBe (amount % Byte.SIZE_BITS)
                bitBuf.readUnsignedBits(amount) shouldBe value
            } finally {
                bitBuf.release()
            }
        }
    }
})