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
private data class ShortTest(
    @JShort(JShortType.DEFAULT) val default: Short,
    @JShort(JShortType.LE) val le: Short,
    @JShort(JShortType.ADD) val add: Short,
    @JShort(JShortType.LE_ADD) val leAdd: Short
)

@ExperimentalSerializationApi
@Serializable
private data class UShortTest(
    @JShort(JShortType.DEFAULT) val default: UShort,
    @JShort(JShortType.LE) val le: UShort,
    @JShort(JShortType.ADD) val add: UShort,
    @JShort(JShortType.LE_ADD) val leAdd: UShort
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationShortTest : StringSpec({
    "Encode/Decode Test" {
        checkAll<Short, Short, Short, Short> { default, le, add, leAdd ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(Short.SIZE_BYTES * 4).apply {
                writeShort(default.toInt())
                writeShortLE(le.toInt())
                writeShortAdd(add.toInt())
                writeShortLEAdd(leAdd.toInt())
            }
            try {
                val expectedTest = ShortTest(default, le, add, leAdd)
                val actualByteBuf = JagMessage.encodeToByteBuf(ShortTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(ShortTest.serializer(), expectedByteBuf)
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
        checkAll<UShort, UShort, UShort, UShort> { default, le, add, leAdd ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(UShort.SIZE_BYTES * 4).apply {
                writeShort(default.toInt())
                writeShortLE(le.toInt())
                writeShortAdd(add.toInt())
                writeShortLEAdd(leAdd.toInt())
            }
            try {
                val expectedTest = UShortTest(default, le, add, leAdd)
                val actualByteBuf = JagMessage.encodeToByteBuf(UShortTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(UShortTest.serializer(), expectedByteBuf)
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