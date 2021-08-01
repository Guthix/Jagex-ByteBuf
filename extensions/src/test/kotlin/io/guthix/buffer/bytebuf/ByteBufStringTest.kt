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
package io.guthix.buffer.bytebuf

import io.guthix.buffer.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import java.nio.charset.Charset

private suspend fun doStringRWTest(
    charset: Charset,
    writer: ByteBuf.(String, Charset) -> ByteBuf,
    reader: ByteBuf.(Charset) -> String
) = checkAll(Arb.list(Arb.string(), collectionSizeRange)) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer()
    try {
        testData.forEach { expected -> buf.writer(expected, charset) }
        testData.forEach { expected ->
            val read = buf.reader(charset)
            read shouldBe expected
        }
    } finally {
        buf.release()
    }
}

private suspend fun doVersionedStringRWTest(
    charset: Charset,
    writer: ByteBuf.(String, Charset, Int) -> ByteBuf,
    reader: ByteBuf.(Charset, Int) -> String
) = checkAll(Arb.list(Arb.string(), collectionSizeRange)) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer()
    val versions = Arb.byte().take(testData.size).toList()
    try {
        testData.forEachIndexed  { i, expected -> buf.writer(expected, charset, versions[i].toInt()) }
        testData.forEachIndexed { i, expected ->
            val read = buf.reader(charset, versions[i].toInt())
            read shouldBe expected
        }
    } finally {
        buf.release()
    }
}

class ByteBufStringTest : StringSpec({
    "Read/Write String windows1252" { doStringRWTest(windows1252, ByteBuf::writeString, ByteBuf::readString) }
    "Read/Write String cesu8" { doStringRWTest(cesu8, ByteBuf::writeString, ByteBuf::readString) }
    "Read/Write versioned String windows1252" {
        doStringRWTest(windows1252, ByteBuf::writeVersionedString, ByteBuf::readVersionedString)
    }
    "Read/Write versioned String cesu8" {
        doStringRWTest(cesu8, ByteBuf::writeVersionedString, ByteBuf::readVersionedString)
    }
})