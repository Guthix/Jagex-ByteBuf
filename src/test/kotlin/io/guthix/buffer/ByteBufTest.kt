package io.guthix.buffer

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.netty.buffer.Unpooled

class ByteBufTest : StringSpec({
    "Short with add transformation" {
        val value = 4930
        val buf = Unpooled.buffer(2)
        buf.writeShortAdd(value)
        buf.readShortAdd().toInt() shouldBe value
    }

    "Little endian short with add transformation " {
        val value = 5439
        val buf = Unpooled.buffer(2)
        buf.writeShortAddLE(value)
        buf.readShortAddLE().toInt() shouldBe value
    }
})