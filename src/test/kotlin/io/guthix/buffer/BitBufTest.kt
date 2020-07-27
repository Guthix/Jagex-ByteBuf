/*
 * Copyright 2018-2020 Guthix
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
import io.netty.buffer.Unpooled

class BitBufTest : StringSpec({
    "Write value smaller than a byte" {
        val bitBuf = Unpooled.buffer(1).toBitMode()
        bitBuf.writeBits(value = 3, amount = 2)
        bitBuf.relativeBitWriterIndex shouldBe 2
        bitBuf.readBits(amount = 2) shouldBe 3
        bitBuf.relativeBitReaderIndex shouldBe 2
    }

    "Write 2 values smaller than a byte after each other" {
        val bitBuf = Unpooled.buffer(1).toBitMode()
        bitBuf.writeBits(value = 3, amount = 2)
        bitBuf.relativeBitWriterIndex shouldBe 2
        bitBuf.readBits(amount = 2) shouldBe 3
        bitBuf.relativeBitReaderIndex shouldBe 2
        bitBuf.writeBits(value = 4, amount = 3)
        bitBuf.relativeBitWriterIndex shouldBe 5
        bitBuf.readBits(amount = 3) shouldBe 4
        bitBuf.relativeBitReaderIndex shouldBe 5
    }

    "Write 3 values after each other" {
        val bitBuf = Unpooled.buffer(5).toBitMode()
        bitBuf.writeBits(value = 2348, amount = 13)
        bitBuf.relativeBitWriterIndex shouldBe 5
        bitBuf.readBits(amount = 13) shouldBe 2348
        bitBuf.relativeBitReaderIndex shouldBe 5
        bitBuf.writeBits(value = 524287, amount = 19)
        bitBuf.relativeBitWriterIndex shouldBe 0
        bitBuf.readBits(amount = 19) shouldBe 524287
        bitBuf.relativeBitReaderIndex shouldBe 0
        bitBuf.writeBits(value = 1, amount = 2)
        bitBuf.relativeBitWriterIndex shouldBe 2
        bitBuf.readBits(amount = 2) shouldBe 1
        bitBuf.relativeBitReaderIndex shouldBe 2
    }

    "Write 2000 times 18 bits" {
        val bitBuf = Unpooled.buffer(4500).toBitMode()
        for(i in 0 until 2000) bitBuf.writeBits(3, 18)
        for(i in 0 until 2000) bitBuf.readBits(18) shouldBe 3
        bitBuf.toByteMode().readerIndex() shouldBe 4500
        bitBuf.toByteMode().writerIndex() shouldBe 4500
    }
})