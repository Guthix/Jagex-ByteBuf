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

import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.netty.buffer.Unpooled

class BitBufTest : StringSpec({
    "Write value smaller than a byte" {
        val bitBuf = Unpooled.buffer(1).toBitMode()
        bitBuf.writeBits(value = 3, amount = 2)
        bitBuf.bitWriterPos shouldBe 2
        bitBuf.readBits(amount = 2) shouldBe 3
        bitBuf.bitReaderPos shouldBe 2
    }

    "Write 2 values smaller than a byte after each other" {
        val bitBuf = Unpooled.buffer(1).toBitMode()
        bitBuf.writeBits(value = 3, amount = 2)
        bitBuf.bitWriterPos shouldBe 2
        bitBuf.readBits(amount = 2) shouldBe 3
        bitBuf.bitReaderPos shouldBe 2
        bitBuf.writeBits(value = 4, amount = 3)
        bitBuf.bitWriterPos shouldBe 5
        bitBuf.readBits(amount = 3) shouldBe 4
        bitBuf.bitReaderPos shouldBe 5
    }

    "Write 3 values after each other" {
        val bitBuf = Unpooled.buffer(5).toBitMode()
        bitBuf.writeBits(value = 2348, amount = 13)
        bitBuf.bitWriterPos shouldBe 5
        bitBuf.readBits(amount = 13) shouldBe 2348
        bitBuf.bitReaderPos shouldBe 5
        bitBuf.writeBits(value = 524287, amount = 19)
        bitBuf.bitWriterPos shouldBe 0
        bitBuf.readBits(amount = 19) shouldBe 524287
        bitBuf.bitReaderPos shouldBe 0
        bitBuf.writeBits(value = 1, amount = 2)
        bitBuf.bitWriterPos shouldBe 2
        bitBuf.readBits(amount = 2) shouldBe 1
        bitBuf.bitReaderPos shouldBe 2
    }

    "Write 2000 times 18 bits" {
        val bitBuf = Unpooled.buffer(4500).toBitMode()
        for(i in 0 until 2000) bitBuf.writeBits(3, 18)
        for(i in 0 until 2000) bitBuf.readBits(18) shouldBe 3
        bitBuf.toByteMode().readerIndex() shouldBe 4500
        bitBuf.toByteMode().writerIndex() shouldBe 4500
    }
})