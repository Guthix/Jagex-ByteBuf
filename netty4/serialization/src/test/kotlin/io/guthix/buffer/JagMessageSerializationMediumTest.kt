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
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.uInt
import io.kotest.property.checkAll
import io.netty.buffer.ByteBufAllocator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
private data class MediumTest(
    @JMedium(JMediumType.DEFAULT) val default: Int,
    @JMedium(JMediumType.LME) val lme: Int,
    @JMedium(JMediumType.RME) val rme: Int
)

@ExperimentalSerializationApi
@Serializable
private data class UMediumTest(
    @JMedium(JMediumType.DEFAULT) val default: UInt,
    @JMedium(JMediumType.LME) val le: UInt,
    @JMedium(JMediumType.RME) val add: UInt
)

private fun Arb.Companion.medium() = int(Medium.MIN_VALUE, Medium.MAX_VALUE)

private fun Arb.Companion.uMedium() = uInt(UMedium.MIN_VALUE, UMedium.MAX_VALUE)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationMediumTest : StringSpec({
    "Encode/Decode Test" {
        checkAll(Arb.medium(), Arb.medium(), Arb.medium()) { default, lme, rme ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(Medium.SIZE_BYTES * 3).apply {
                writeMedium(default)
                writeMediumLME(lme)
                writeMediumRME(rme)
            }
            try {
                val expectedTest = MediumTest(default, lme, rme)
                val actualByteBuf = JagMessage.encodeToByteBuf(MediumTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(MediumTest.serializer(), expectedByteBuf)
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
        checkAll(Arb.uMedium(), Arb.uMedium(), Arb.uMedium()) { default, lme, rme ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(UMedium.SIZE_BYTES * 3).apply {
                writeMedium(default.toInt())
                writeMediumLME(lme.toInt())
                writeMediumRME(rme.toInt())
            }
            try {
                val expectedTest = UMediumTest(default, lme, rme)
                val actualByteBuf = JagMessage.encodeToByteBuf(UMediumTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(UMediumTest.serializer(), expectedByteBuf)
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