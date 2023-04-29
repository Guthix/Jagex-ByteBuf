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

import org.guthix.buffer.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeNonNegative
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.Shrinker
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import kotlin.random.nextUInt

private suspend fun doIntSmartRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Int
) = checkAll(Arb.intArray(collectionSizeArb, Arb.int(Smart.MIN_INT_VALUE, Smart.MAX_INT_VALUE))) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Short.SIZE_BYTES)
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
private suspend fun doUIntSmartRWTest(
    writer: ByteBuf.(Int) -> ByteBuf,
    reader: ByteBuf.() -> Int
) = checkAll(
    Arb.uIntArray(collectionSizeArb, Arb.uInt(USmart.MIN_INT_VALUE.toUInt(), USmart.MAX_INT_VALUE.toUInt()))
) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Short.SIZE_BYTES)
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

fun Arb.Companion.nullableUInt(min: UInt = UInt.MIN_VALUE, max: UInt = UInt.MAX_VALUE) = nullableUInt(min..max)

fun Arb.Companion.nullableUInt(range: UIntRange = UInt.MIN_VALUE..UInt.MAX_VALUE): Arb<UInt?> {
    val edges = listOf(range.first, 1u, range.last).filter { it in range }.distinct()
    return arbitrary(edges, NullableUIntShrinker(range)) {
        if (it.random.nextFloat() < 0.40) null else it.random.nextUInt(range)
    }
}
class NullableUIntShrinker(private val range: UIntRange) : Shrinker<UInt?> {
    override fun shrink(value: UInt?): List<UInt?> = when (value) {
        null -> emptyList()
        0u -> emptyList()
        1u -> listOf(0u).filter { it in range }
        else -> {
            val a = listOf(0u, 1u, value / 3u, null, value / 2u, value * 2u / 3u)
            val b = (1u..5u).map { value - it }.reversed().filter { it > 0u }
            (a + b).distinct().filterNot { it == value }.filter { it in range }
        }
    }
}

@ExperimentalUnsignedTypes
private suspend fun doNullableUIntSmartRWTest(
    writer: ByteBuf.(Int?) -> ByteBuf,
    reader: ByteBuf.() -> Int?
) = checkAll(
    Arb.list(Arb.nullableUInt(USmart.MIN_INT_VALUE.toUInt(), USmart.MAX_INT_VALUE.toUInt()))
) { testData ->
    val buf = ByteBufAllocator.DEFAULT.buffer(testData.size * Short.SIZE_BYTES)
    try {
        testData.forEach { expected -> buf.writer(expected?.toInt()) }
        testData.forEach { expected ->
            val read = buf.reader()
            read?.shouldBeNonNegative()
            read shouldBe expected?.toInt()
        }
    } finally {
        buf.release()
    }
}

@ExperimentalUnsignedTypes
class ByteBufIntSmartTest : StringSpec({
    "Read/Write Int Smart" { doIntSmartRWTest(ByteBuf::writeIntSmart, ByteBuf::readIntSmart) }
    "Unsigned Read/Write Int Smart" {
        doUIntSmartRWTest(ByteBuf::writeUnsignedIntSmart, ByteBuf::readUnsignedIntSmart)
    }
    "Unsigned Read/Write Nullable Int Smart" {
        doNullableUIntSmartRWTest(ByteBuf::writeNullableUnsignedIntSmart, ByteBuf::readNullableUnsignedIntSmart)
    }
})