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
import io.netty.buffer.Unpooled

private suspend fun doByteArrayGSTest(
    setter: ByteBuf.(Int, ByteArray) -> ByteBuf,
    getter: ByteBuf.(Int, Int) -> ByteArray
) = checkAll(Arb.list(Arb.byteArray(collectionSizeArb, Arb.byte()), collectionSizeRange)) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.sumOf { it.size } * Byte.SIZE_BYTES)
    var sizeSum = 0
    try {
        testData.forEach { expected ->
            buf.setter(sizeSum, expected)
            sizeSum += expected.size
        }
        sizeSum = 0
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(sizeSum, testData[i].size)
            get shouldBe expected
            sizeSum += expected.size
        }
    } finally {
        buf.release()
    }
}

private suspend fun doByteArrayByteBufGSTest(
    setter: ByteBuf.(Int, ByteBuf) -> ByteBuf,
    getter: ByteBuf.(Int, Int) -> ByteArray
) = checkAll(Arb.list(Arb.byteArray(collectionSizeArb, Arb.byte()), collectionSizeRange)) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.sumOf { it.size } * Byte.SIZE_BYTES)
    var sizeSum = 0
    try {
        testData.forEach { expected ->
            val bufExpected = Unpooled.wrappedBuffer(expected)
            buf.setter(sizeSum, bufExpected)
            sizeSum += expected.size
        }
        sizeSum = 0
        testData.forEachIndexed { i, expected ->
            val get = buf.getter(sizeSum, testData[i].size)
            get shouldBe expected
            sizeSum += expected.size
        }
    } finally {
        buf.release()
    }
}


private suspend fun doByteArrayRWTest(
    writer: ByteBuf.(ByteArray) -> ByteBuf,
    reader: ByteBuf.(Int) -> ByteArray
) = checkAll(Arb.list(Arb.byteArray(collectionSizeArb, Arb.byte()), collectionSizeRange)) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.sumOf { it.size } * Byte.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected) }
        testData.forEachIndexed { i, expected ->
            val read = buf.reader(testData[i].size)
            read shouldBe expected
        }
    } finally {
        buf.release()
    }
}

private suspend fun doByteArrayByteBufRWTest(
    writer: ByteBuf.(ByteBuf) -> ByteBuf,
    reader: ByteBuf.(Int) -> ByteArray
) = checkAll(Arb.list(Arb.byteArray(collectionSizeArb, Arb.byte()), collectionSizeRange)) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.sumOf { it.size } * Byte.SIZE_BYTES)
    try {
        testData.forEach { expected ->
            val bufExpected = Unpooled.wrappedBuffer(expected)
            buf.writer(bufExpected)
        }
        testData.forEachIndexed { i, expected ->
            val read = buf.reader(testData[i].size)
            read shouldBe expected
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
class ByteBufBytArrayTest : StringSpec({
    "Get/Set Bytes Reversed" { doByteArrayGSTest(ByteBuf::setBytesReversed, ByteBuf::getBytesReversed) }
    "Read/Write Bytes Reversed" { doByteArrayRWTest(ByteBuf::writeBytesReversed, ByteBuf::readBytesReversed) }
    "Get/Set Bytes ByteBuf Reversed" { doByteArrayByteBufGSTest(ByteBuf::setBytesReversed, ByteBuf::getBytesReversed) }
    "Read/Write Bytes ByteBuf Reversed" {
        doByteArrayByteBufRWTest(ByteBuf::writeBytesReversed, ByteBuf::readBytesReversed)
    }

    "Get/Set Bytes Add" { doByteArrayGSTest(ByteBuf::setBytesAdd, ByteBuf::getBytesAdd) }
    "Read/Write Bytes Add" { doByteArrayRWTest(ByteBuf::writeBytesAdd, ByteBuf::readBytesAdd) }
    "Get/Set Bytes ByteBuf Add" { doByteArrayByteBufGSTest(ByteBuf::setBytesAdd, ByteBuf::getBytesAdd) }
    "Read/Write Bytes ByteBuf Add" { doByteArrayByteBufRWTest(ByteBuf::writeBytesAdd, ByteBuf::readBytesAdd) }

    "Get/Set Bytes Reversed Add" { doByteArrayGSTest(ByteBuf::setBytesReversedAdd, ByteBuf::getBytesReversedAdd) }
    "Read/Write Bytes Reversed Add" { doByteArrayRWTest(ByteBuf::writeBytesReversedAdd, ByteBuf::readBytesReversedAdd) }
    "Get/Set Bytes ByteBuf Reversed Add" {
        doByteArrayByteBufGSTest(ByteBuf::setBytesReversedAdd, ByteBuf::getBytesReversedAdd)
    }
    "Read/Write Bytes ByteBuf Reversed Add" {
        doByteArrayByteBufRWTest(ByteBuf::writeBytesReversedAdd, ByteBuf::readBytesReversedAdd)
    }
})