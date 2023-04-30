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
package org.guthix.buffer.bytebuf

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeNonNegative
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import org.guthix.buffer.*

private suspend fun doShortSmartRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Short
) = checkAll(
    Arb.shortArray(collectionSizeArb, Arb.short(Smart.MIN_SHORT_VALUE.toShort(), Smart.MAX_SHORT_VALUE.toShort()))
) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Short.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected.toInt()) }
        testData.forEach { expected ->
            val read = buf.reader()
            read shouldBe expected
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doUShortSmartRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Short
) = checkAll(
    Arb.uShortArray(collectionSizeArb, Arb.uShort(USmart.MIN_SHORT_VALUE.toUShort(), USmart.MAX_SHORT_VALUE.toUShort()))
) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Short.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected.toInt()) }
        testData.forEach { expected ->
            val read = buf.reader()
            read.toInt().shouldBeNonNegative()
            read shouldBe expected.toInt()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doIncrShortSmartRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Int
) = checkAll(Arb.intArray(collectionSizeArb, Arb.int(0, 100_000))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer()
    try {
        testData.forEach { expected -> buf.writer(expected) }
        testData.forEach { expected ->
            val read = buf.reader()
            read.shouldBeNonNegative()
            read shouldBe expected
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
class ByteBufShortSmartTest : StringSpec({
    "Read/Write Short Smart" { doShortSmartRWTest(ByteBuf::writeShortSmart, ByteBuf::readShortSmart) }
    "Unsigned Read/Write Short Smart" {
        doUShortSmartRWTest(ByteBuf::writeUnsignedShortSmart, ByteBuf::readUnsignedShortSmart)
    }
    "Unsigned Read/Write Incr Short Smart" {
        doIncrShortSmartRWTest(ByteBuf::writeIncrShortSmart, ByteBuf::readIncrShortSmart)
    }
})