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
private data class StringTest(
    @JString(JCharSet.CP_1252) val default: String,
    @JString(JCharSet.CESU8) val cesu8: String,
    @JVersionedString(JCharSet.CP_1252, version = 0) val versioned: String
)

@ExperimentalUnsignedTypes
@ExperimentalSerializationApi
class JagMessageSerializationStringTest : StringSpec({
    "Encode/Decode Test" {
        checkAll<String, String, String> { default, cesu8, versioned ->
            val expectedByteBuf = ByteBufAllocator.DEFAULT.jBuffer(0).apply {
                writeString(default)
                writeString(cesu8, Charsets.CESU_8)
                writeVersionedString(versioned, version = 0)
            }
            try {
                val expectedTest = StringTest(default, cesu8, versioned)
                val actualByteBuf = JagMessage.encodeToByteBuf(StringTest.serializer(), expectedTest)
                try {
                    actualByteBuf shouldBe expectedByteBuf
                    val actualTest = JagMessage.decodeFromByteBuf(StringTest.serializer(), expectedByteBuf)
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