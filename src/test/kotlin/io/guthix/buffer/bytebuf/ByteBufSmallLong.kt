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

private suspend fun doSmallLongGSTest(
    setter: ByteBuf.(Int, Long) -> ByteBuf,
    getter: ByteBuf.(Int) -> Long
) = checkAll(Arb.longArray(arraySizeRange, Arb.long(SmallLong.MIN_VALUE, SmallLong.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * SmallLong.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * SmallLong.SIZE_BYTES, expected) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * SmallLong.SIZE_BYTES)
            get shouldBe expected
        }
    } finally {
        buf.release()
    }
}

private suspend fun doSmallLongRWTest(
    writer: ByteBuf.(Long) -> ByteBuf,
    reader: ByteBuf.() -> Long
) = checkAll(Arb.longArray(arraySizeRange, Arb.long(SmallLong.MIN_VALUE, SmallLong.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * SmallLong.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected) }
        testData.forEach { expected ->
            val read = buf.reader()
            read shouldBe expected
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doUSmallLongGSTest(
    setter: ByteBuf.(Int, Long) -> ByteBuf,
    getter: ByteBuf.(Int) -> Long
) = checkAll(Arb.uLongArray(arraySizeRange, Arb.uLong(USmallLong.MIN_VALUE, USmallLong.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * USmallLong.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * USmallLong.SIZE_BYTES, expected.toLong()) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * USmallLong.SIZE_BYTES)
            get.shouldBeNonNegative()
            get shouldBe expected.toLong()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doUSmallLongRWTest(
    writer: ByteBuf.(Long) -> ByteBuf,
    reader: ByteBuf.() -> Long
) = checkAll(Arb.uLongArray(arraySizeRange, Arb.uLong(USmallLong.MIN_VALUE, USmallLong.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * USmallLong.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected.toLong()) }
        testData.forEach { expected ->
            val read = buf.reader()
            read.shouldBeNonNegative()
            read shouldBe expected.toLong()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
class ByteBufSmallLongTest : StringSpec({
    "Get/Set SmallLong LME" { doSmallLongGSTest(ByteBuf::setSmallLong, ByteBuf::getSmallLong) }
    "Read/Write SmallLong LME" { doSmallLongRWTest(ByteBuf::writeSmallLong, ByteBuf::readSmallLong) }
    "Unsigned Get/Set SmallLong LME" { doUSmallLongGSTest(ByteBuf::setSmallLong, ByteBuf::getUnsignedSmallLong) }
    "Unsigned Read/Write SmallLong LME" { doUSmallLongRWTest(ByteBuf::writeSmallLong, ByteBuf::readUnsignedSmallLong) }
})