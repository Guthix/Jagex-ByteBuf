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

object Medium {
    const val SIZE_BYTES: Int = 3
    const val SIZE_BITS: Int = SIZE_BYTES * Byte.SIZE_BITS
    const val MAX_VALUE: Int = 8_388_607
    const val MIN_VALUE: Int = -8_388_608
}

object UMedium {
    const val SIZE_BYTES: Int = Medium.SIZE_BYTES
    const val SIZE_BITS: Int = Medium.SIZE_BITS
    const val MAX_VALUE: UInt = 16_777_215u
    const val MIN_VALUE: UInt = 0u
}

private suspend fun doMediumGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Int
) = checkAll(Arb.intArray(arraySizeRange, Arb.int(Medium.MIN_VALUE, Medium.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Medium.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * Medium.SIZE_BYTES, expected) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * Medium.SIZE_BYTES)
            get shouldBe expected
        }
    } finally {
        buf.release()
    }
}

private suspend fun doMediumRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Int
) = checkAll(Arb.intArray(arraySizeRange, Arb.int(Medium.MIN_VALUE, Medium.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Medium.SIZE_BYTES)
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
private suspend fun doUMediumGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Int
) = checkAll(Arb.uIntArray(arraySizeRange, Arb.uInt(UMedium.MIN_VALUE, UMedium.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UMedium.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i * UMedium.SIZE_BYTES, expected.toInt()) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i * UMedium.SIZE_BYTES)
            get shouldBe expected.toInt()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doUMediumRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Int
) = checkAll(Arb.uIntArray(arraySizeRange, Arb.uInt(UMedium.MIN_VALUE, UMedium.MAX_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UMedium.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected.toInt()) }
        testData.forEach { expected ->
            val read = buf.reader()
            read shouldBe expected.toInt()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
class ByteBufMediumTest : StringSpec({
    "Get/Set Medium LME" { doMediumGSTest(ByteBuf::setMediumLME, ByteBuf::getMediumLME) }
    "Read/Write Medium LME" { doMediumRWTest(ByteBuf::writeMediumLME, ByteBuf::readMediumLME) }
    "Unsigned Get/Set Medium LME" { doUMediumGSTest(ByteBuf::setMediumLME, ByteBuf::getUnsignedMediumLME) }
    "Unsigned Read/Write Medium LME" { doUMediumRWTest(ByteBuf::writeMediumLME, ByteBuf::readUnsignedMediumLME) }

    "Get/Set Medium RME" { doMediumGSTest(ByteBuf::setMediumRME, ByteBuf::getMediumRME) }
    "Read/Write Medium RME" { doMediumRWTest(ByteBuf::writeMediumRME, ByteBuf::readMediumRME) }
    "Unsigned Get/Set Medium RME" { doUMediumGSTest(ByteBuf::setMediumRME, ByteBuf::getUnsignedMediumRME) }
    "Unsigned Read/Write Medium RME" { doUMediumRWTest(ByteBuf::writeMediumRME, ByteBuf::readUnsignedMediumRME) }
})