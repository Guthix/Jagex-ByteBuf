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
package org.guthix.buffer

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.uShort
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
private data class ShortSmartTest(
    @JShortSmart val default: Short
)

@ExperimentalSerializationApi
@Serializable
private data class UShortSmartTest(
    @JShortSmart val default: UShort
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationShortSmartTest : StringSpec({
    "Encode/Decode Test" {
        checkAll(Arb.short(Smart.MIN_SHORT_VALUE.toShort(), Smart.MAX_SHORT_VALUE.toShort())) { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(Short.SIZE_BYTES).apply {
                writeShortSmart(default.toInt())
            }
            try {
                val expectedTest = ShortSmartTest(default)
                val actualByteBuf = JagMessage.encodeToByteBuf(ShortSmartTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(ShortSmartTest.serializer(), expectedByteBuf)
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
        checkAll(Arb.uShort(USmart.MIN_SHORT_VALUE.toUShort(), USmart.MAX_SHORT_VALUE.toUShort())) { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(UShort.SIZE_BYTES).apply {
                writeUShortSmart(default.toInt())
            }
            try {
                val expectedTest = UShortSmartTest(default)
                val actualByteBuf = JagMessage.encodeToByteBuf(UShortSmartTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(UShortSmartTest.serializer(), expectedByteBuf)
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