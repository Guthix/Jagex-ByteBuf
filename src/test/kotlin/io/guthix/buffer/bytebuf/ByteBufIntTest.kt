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
import io.kotest.matchers.ints.shouldBeNonNegative
import io.kotest.matchers.ints.shouldBePositive
import io.kotest.matchers.longs.shouldBePositive
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator

private suspend fun doIntGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Int
) = checkAll(Arb.intArray(arraySizeRange, Arb.int())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Int.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * Int.SIZE_BYTES, expected) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * Int.SIZE_BYTES)
            get shouldBe expected
        }
    } finally {
        buf.release()
    }
}

private suspend fun doIntRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Int
) = checkAll(Arb.intArray(arraySizeRange, Arb.int())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Int.SIZE_BYTES)
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
private suspend fun doUIntGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Long
) = checkAll(Arb.uIntArray(arraySizeRange, Arb.uInt())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UInt.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * UInt.SIZE_BYTES, expected.toInt()) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * UInt.SIZE_BYTES)
            get.shouldBeNonNegative()
            get shouldBe expected.toLong()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doUIntRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Long
) = checkAll(Arb.uIntArray(arraySizeRange, Arb.uInt())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UInt.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected.toInt()) }
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
class ByteBufIntTest : StringSpec({
    "Get/Set Int ME" { doIntGSTest(ByteBuf::setIntME, ByteBuf::getIntME) }
    "Read/Write Int ME" { doIntRWTest(ByteBuf::writeIntME, ByteBuf::readIntME) }
    "Unsigned Get/Set Int ME" { doUIntGSTest(ByteBuf::setIntME, ByteBuf::getUnsignedIntME) }
    "Unsigned Read/Write Int ME" { doUIntRWTest(ByteBuf::writeIntME, ByteBuf::readUnsignedIntME) }

    "Get/Set Int IME" { doIntGSTest(ByteBuf::setIntIME, ByteBuf::getIntIME) }
    "Read/Write Int IME" { doIntRWTest(ByteBuf::writeIntIME, ByteBuf::readIntIME) }
    "Unsigned Get/Set Int IME" { doUIntGSTest(ByteBuf::setIntIME, ByteBuf::getUnsignedIntIME) }
    "Unsigned Read/Write Int IME" { doUIntRWTest(ByteBuf::writeIntIME, ByteBuf::readUnsignedIntIME) }
})