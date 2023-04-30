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

private suspend fun doShortGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Short
) = checkAll(Arb.shortArray(collectionSizeArb, Arb.short())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Short.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * Short.SIZE_BYTES, expected.toInt()) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * Short.SIZE_BYTES)
            get shouldBe expected
        }
    } finally {
        buf.release()
    }
}

private suspend fun doShortRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Short
) = checkAll(Arb.shortArray(collectionSizeArb, Arb.short())) { testData ->
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
private suspend fun doUShortGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Int
) = checkAll(Arb.uShortArray(collectionSizeArb, Arb.uShort())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UShort.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * UShort.SIZE_BYTES, expected.toInt()) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * UShort.SIZE_BYTES)
            get.shouldBeNonNegative()
            get shouldBe expected.toInt()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doUShortRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Int
) = checkAll(Arb.uShortArray(collectionSizeArb, Arb.uShort())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UShort.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected.toInt()) }
        testData.forEach { expected ->
            val read = buf.reader()
            read.shouldBeNonNegative()
            read shouldBe expected.toInt()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
class ByteBufShortTest : StringSpec({
    "Get/Set Short add" { doShortGSTest(ByteBuf::setShortAdd, ByteBuf::getShortAdd) }
    "Read/Write Short add" { doShortRWTest(ByteBuf::writeShortAdd, ByteBuf::readShortAdd) }
    "Unsigned Get/Set Short add" { doUShortGSTest(ByteBuf::setShortAdd, ByteBuf::getUnsignedShortAdd) }
    "Unsigned Read/Write Short add" { doUShortRWTest(ByteBuf::writeShortAdd, ByteBuf::readUnsignedShortAdd) }

    "Get/Set Short LE add" { doShortGSTest(ByteBuf::setShortLEAdd, ByteBuf::getShortLEAdd) }
    "Read/Write Short LE add" { doShortRWTest(ByteBuf::writeShortLEAdd, ByteBuf::readShortLEAdd) }
    "Unsigned Get/Set Short LE add" { doUShortGSTest(ByteBuf::setShortLEAdd, ByteBuf::getUnsignedShortLEAdd) }
    "Unsigned Read/Write Short LE add" { doUShortRWTest(ByteBuf::writeShortLEAdd, ByteBuf::readUnsignedShortLEAdd) }
})