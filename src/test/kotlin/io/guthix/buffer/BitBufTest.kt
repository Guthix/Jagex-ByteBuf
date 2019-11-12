package io.guthix.buffer

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
})