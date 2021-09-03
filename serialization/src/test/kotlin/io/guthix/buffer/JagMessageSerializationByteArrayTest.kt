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
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
private data class ByteArrayTest(
    @JByteArray(10, JByteArrayType.DEFAULT) val default: ByteArray,
    @JByteArray(10, JByteArrayType.REVERSED) val reversed: ByteArray,
    @JByteArray(10, JByteArrayType.ADD) val add: ByteArray,
    @JByteArray(10, JByteArrayType.REVERSED_ADD) val reversedAdd: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ByteArrayTest
        if (!default.contentEquals(other.default)) return false
        if (!reversed.contentEquals(other.reversed)) return false
        if (!add.contentEquals(other.add)) return false
        if (!reversedAdd.contentEquals(other.reversedAdd)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = default.contentHashCode()
        result = 31 * result + reversed.contentHashCode()
        result = 31 * result + add.contentHashCode()
        result = 31 * result + reversedAdd.contentHashCode()
        return result
    }
}

private fun Arb.Companion.byteArray() = byteArray(int(10, 10), byte())

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationByteArrayTest : StringSpec({
    "Encode/Decode Test" {
        checkAll(Arb.byteArray(), Arb.byteArray(), Arb.byteArray(), Arb.byteArray()) { default, reversed, add, reversedAdd ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(0).apply {
                writeBytes(default)
                writeBytesReversed(reversed)
                writeBytesAdd(add)
                writeBytesReversedAdd(reversedAdd)
            }
            val expectedTest = ByteArrayTest(default, reversed, add, reversedAdd)
            val actualByteBuf = JagMessage.encodeToByteBuf(ByteArrayTest.serializer(), expectedTest)
            actualByteBuf shouldBe expectedByteBuf
            val actualTest = JagMessage.decodeFromByteBuf(ByteArrayTest.serializer(), expectedByteBuf)
            actualTest shouldBe expectedTest
        }
    }
})