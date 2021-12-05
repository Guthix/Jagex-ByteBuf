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
private data class IntTest(
    @JInt(JIntType.DEFAULT) val default: Int,
    @JInt(JIntType.ME) val me: Int,
    @JInt(JIntType.IME) val ime: Int,
    @JInt(JIntType.LE) val le: Int
)

@ExperimentalSerializationApi
@Serializable
private data class UIntTest(
    @JInt(JIntType.DEFAULT) val default: UInt,
    @JInt(JIntType.ME) val me: UInt,
    @JInt(JIntType.IME) val ime: UInt,
    @JInt(JIntType.LE) val le: UInt
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationIntTest : StringSpec({
    "Encode/Decode Test" {
        checkAll<Int, Int, Int, Int> { default, me, ime, le ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(Int.SIZE_BYTES * 4).apply {
                writeInt(default)
                writeIntME(me)
                writeIntIME(ime)
                writeIntLE(le)
            }
            try {
                val expectedTest = IntTest(default, me, ime, le)
                val actualByteBuf = JagMessage.encodeToByteBuf(IntTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(IntTest.serializer(), expectedByteBuf)
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
        checkAll<UInt, UInt, UInt, UInt> { default, me, ime, le ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(UInt.SIZE_BYTES * 4).apply {
                writeInt(default.toInt())
                writeIntME(me.toInt())
                writeIntIME(ime.toInt())
                writeIntLE(le.toInt())
            }
            try {
                val expectedTest = UIntTest(default, me, ime, le)
                val actualByteBuf = JagMessage.encodeToByteBuf(UIntTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(UIntTest.serializer(), expectedByteBuf)
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