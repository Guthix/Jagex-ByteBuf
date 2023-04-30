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
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.uByte
import io.kotest.property.arbitrary.uByteArray
import io.kotest.property.checkAll
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import org.guthix.buffer.*

private suspend fun doByteGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Byte
) = checkAll(Arb.byteArray(collectionSizeArb, Arb.byte())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Byte.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i, expected.toInt()) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i)
            get shouldBe expected
        }
    } finally {
        buf.release()
    }
}

private suspend fun doByteRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Byte
) = checkAll(Arb.byteArray(collectionSizeArb, Arb.byte())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Byte.SIZE_BYTES)
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
private suspend fun doUByteGSTest(
    setter: ByteBuf.(Int, Int) -> ByteBuf,
    getter: ByteBuf.(Int) -> Short
) = checkAll(Arb.uByteArray(collectionSizeArb, Arb.uByte())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UByte.SIZE_BYTES)
    try {
        testData.forEachIndexed { i, expected -> buf.setter(i, expected.toInt()) }
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(i)
            get.toInt().shouldBeNonNegative()
            get shouldBe expected.toShort()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
private suspend fun doUByteRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Short
) = checkAll(Arb.uByteArray(collectionSizeArb, Arb.uByte())) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * UByte.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected.toInt()) }
        testData.forEach { expected ->
            val read = buf.reader()
            read.toInt().shouldBeNonNegative()
            read shouldBe expected.toShort()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
class ByteBufByteTest : StringSpec({
    "Get/Set Byte neg" { doByteGSTest(ByteBuf::setByteNeg, ByteBuf::getByteNeg) }
    "Read/Write Byte neg" { doByteRWTest(ByteBuf::writeByteNeg, ByteBuf::readByteNeg) }
    "Unsigned Get/Set Byte neg" { doUByteGSTest(ByteBuf::setByteNeg, ByteBuf::getUnsignedByteNeg) }
    "Unsigned Read/Write Byte neg" { doUByteRWTest(ByteBuf::writeByteNeg, ByteBuf::readUnsignedByteNeg) }

    "Get/Set Byte add" { doByteGSTest(ByteBuf::setByteAdd, ByteBuf::getByteAdd) }
    "Read/Write Byte add" { doByteRWTest(ByteBuf::writeByteAdd, ByteBuf::readByteAdd) }
    "Unsigned Get/Set Byte add" { doUByteGSTest(ByteBuf::setByteAdd, ByteBuf::getUnsignedByteAdd) }
    "Unsigned Read/Write Byte add" { doUByteRWTest(ByteBuf::writeByteAdd, ByteBuf::readUnsignedByteAdd) }

    "Get/Set Byte sub" { doByteGSTest(ByteBuf::setByteSub, ByteBuf::getByteSub) }
    "Read/Write Byte sub" { doByteRWTest(ByteBuf::writeByteSub, ByteBuf::readByteSub) }
    "Unsigned Get/Set Byte sub" { doUByteGSTest(ByteBuf::setByteSub, ByteBuf::getUnsignedByteSub) }
    "Unsigned Read/Write Byte sub" { doUByteRWTest(ByteBuf::writeByteSub, ByteBuf::readUnsignedByteSub) }
})