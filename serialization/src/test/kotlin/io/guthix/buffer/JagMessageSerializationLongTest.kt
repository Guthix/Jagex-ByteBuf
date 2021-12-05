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
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
private data class LongTest(
    @JLong val default: Long
)

@ExperimentalSerializationApi
@Serializable
private data class ULongTest(
    @JLong val default: ULong
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationLongTest : StringSpec({
    "Encode/Decode Test" {
        checkAll<Long> { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(Long.SIZE_BYTES).apply {
                writeLong(default)
            }
            try {
                val expectedTest = LongTest(default)
                val actualByteBuf = JagMessage.encodeToByteBuf(LongTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(LongTest.serializer(), expectedByteBuf)
                    actualTest shouldBe expectedTest
                } finally {
                    actualByteBuf.release()
                }
            } finally {
                expectedByteBuf.release()
            }
        }
    }

    "Unsigned Encode/Decode Test" {
        checkAll<ULong> { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(ULong.SIZE_BYTES).apply {
                writeLong(default.toLong())
            }
            try {
                val expectedTest = ULongTest(default)
                val actualByteBuf = JagMessage.encodeToByteBuf(ULongTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(ULongTest.serializer(), expectedByteBuf)
                    actualTest shouldBe expectedTest
                } finally {
                    actualByteBuf.release()
                }
            } finally {
                expectedByteBuf.release()
            }
        }
    }
})