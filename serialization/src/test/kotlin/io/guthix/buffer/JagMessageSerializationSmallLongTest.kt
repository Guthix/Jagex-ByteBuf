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
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.uLong
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
private data class SmallLongTest(
    @JSmallLong val default: Long
)

@ExperimentalSerializationApi
@Serializable
private data class USmallLongTest(
    @JSmallLong val default: ULong
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationSmallLongTest : StringSpec({
    "Encode/Decode Test" {
        checkAll(Arb.long(SmallLong.MIN_VALUE, SmallLong.MAX_VALUE)) { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(SmallLong.SIZE_BYTES).apply {
                writeSmallLong(default)
            }
            val expectedTest = SmallLongTest(default)
            val actualByteBuf = JagMessage.encodeToByteBuf(SmallLongTest.serializer(), expectedTest)
            actualByteBuf shouldBe expectedByteBuf
            val actualTest = JagMessage.decodeFromByteBuf(SmallLongTest.serializer(), expectedByteBuf)
            actualTest shouldBe expectedTest
        }
    }
    
    "Unsigned Encode/Decode Test" {
        checkAll(Arb.uLong(USmallLong.MIN_VALUE, USmallLong.MAX_VALUE)) { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(USmallLong.SIZE_BYTES).apply {
                writeSmallLong(default.toLong())
            }
            val expectedTest = USmallLongTest(default)
            val actualByteBuf = JagMessage.encodeToByteBuf(USmallLongTest.serializer(), expectedTest)
            actualByteBuf shouldBe expectedByteBuf
            val actualTest = JagMessage.decodeFromByteBuf(USmallLongTest.serializer(), expectedByteBuf)
            actualTest shouldBe expectedTest
        }
    }
})