package io.guthix.buffer

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.netty.buffer.Unpooled

class ByteBufTest : StringSpec({
    "Half byte short ADD" {
        val buf = Unpooled.buffer(2)
        buf.writeShortADD(300)
        buf.readShort().toInt() shouldBe 428
    }
})