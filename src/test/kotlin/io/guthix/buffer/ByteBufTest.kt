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
import io.netty.buffer.Unpooled

class ByteBufTest : StringSpec({
    "ShortAdd WR" {
        val value = 4930
        val buf = Unpooled.buffer(2)
        buf.writeShortAdd(value)
        buf.readShortAdd().toInt() shouldBe value
    }

    "ShortAddLE WR" {
        val value = 5439
        val buf = Unpooled.buffer(2)
        buf.writeShortAddLE(value)
        buf.readShortAddLE().toInt() shouldBe value
    }

    "MediumLME WR" {
        val value = 17593
        val buf = Unpooled.buffer(3)
        buf.writeMediumLME(value)
        buf.readUnsignedMediumLME() shouldBe value
    }

    "MediumRME WR" {
        val value = 362
        val buf = Unpooled.buffer(3)
        buf.writeMediumRME(value)
        buf.readUnsignedMediumRME() shouldBe value
    }

    "IncrSmallSmart Byte WR" {
        val value = 30
        val buf = Unpooled.buffer(2)
        buf.writeIncrSmallSmart(value)
        buf.readIncrSmallSmart() shouldBe value
    }

    "IncrSmallSmart Short WR" {
        val value = 12643
        val buf = Unpooled.buffer(2)
        buf.writeIncrSmallSmart(value)
        buf.readIncrSmallSmart() shouldBe value
    }

    "IncrSmallSmart Multi WR" {
        val value = 123843
        val buf = Unpooled.buffer(10)
        buf.writeIncrSmallSmart(value)
        buf.readIncrSmallSmart() shouldBe value
    }
})