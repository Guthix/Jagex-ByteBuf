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