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
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.uInt
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
private data class IntSmartTest(
    @JIntSmart val default: Int
)

@ExperimentalSerializationApi
@Serializable
private data class UIntSmartTest(
    @JIntSmart val default: UInt
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationIntSmartTest : StringSpec({
    "Encode/Decode Test" {
        checkAll(Arb.int(Smart.MIN_INT_VALUE, Smart.MAX_INT_VALUE)) { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(Int.SIZE_BYTES).apply {
                writeIntSmart(default)
            }
            try {
                val expectedTest = IntSmartTest(default)
                val actualByteBuf = JagMessage.encodeToByteBuf(IntSmartTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(IntSmartTest.serializer(), expectedByteBuf)
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
        checkAll(Arb.uInt(USmart.MIN_INT_VALUE.toUInt(), USmart.MAX_INT_VALUE.toUInt())) { default ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(UInt.SIZE_BYTES).apply {
                writeUIntSmart(default.toInt())
            }
            try {
                val expectedTest = UIntSmartTest(default)
                val actualByteBuf = JagMessage.encodeToByteBuf(UIntSmartTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(UIntSmartTest.serializer(), expectedByteBuf)
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