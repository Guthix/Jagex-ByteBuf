package io.guthix.buffer

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.netty.buffer.Unpooled

class ByteBufTest : StringSpec({
    "ShortADD" {
        val value = 4930
        val buf = Unpooled.buffer(2)
        buf.writeShortADD(value)
        buf.readShortADD().toInt() shouldBe value
    }

    "ShortLEADD" {
        val value = 5439
        val buf = Unpooled.buffer(2)
        buf.writeShortLEADD(value)
        buf.readShortLEADD().toInt() shouldBe value
    }
})