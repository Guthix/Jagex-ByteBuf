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
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
private data class ByteTest(
    @JByte(JByteType.DEFAULT) val default: Byte,
    @JByte(JByteType.NEG) val neg: Byte,
    @JByte(JByteType.ADD) val add: Byte,
    @JByte(JByteType.SUB) val sub: Byte,
)

@ExperimentalSerializationApi
@Serializable
private data class UByteTest(
    @JByte(JByteType.DEFAULT) val default: UByte,
    @JByte(JByteType.NEG) val neg: UByte,
    @JByte(JByteType.ADD) val add: UByte,
    @JByte(JByteType.SUB) val sub: UByte,
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationByteTest : StringSpec({
    "Encode/Decode Test" {
        checkAll<Byte, Byte, Byte, Byte> { default, neg, add, sub ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(Byte.SIZE_BYTES * 4).apply {
                writeByte(default.toInt())
                writeByteNeg(neg.toInt())
                writeByteAdd(add.toInt())
                writeByteSub(sub.toInt())
            }
            try {
                val expectedTest = ByteTest(default, neg, add, sub)
                val actualByteBuf = JagMessage.encodeToByteBuf(ByteTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(ByteTest.serializer(), expectedByteBuf)
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
        checkAll<UByte, UByte, UByte, UByte> { default, neg, add, sub ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(UByte.SIZE_BYTES * 4).apply {
                writeByte(default.toInt())
                writeByteNeg(neg.toInt())
                writeByteAdd(add.toInt())
                writeByteSub(sub.toInt())
            }
            try {
                val expectedTest = UByteTest(default, neg, add, sub)
                val actualByteBuf = JagMessage.encodeToByteBuf(UByteTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(UByteTest.serializer(), expectedByteBuf)
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