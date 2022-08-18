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

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int

internal val collectionSizeRange = 0..10

internal val collectionSizeArb = Arb.int(collectionSizeRange)

fun Long.shouldBeNonNegative(): Long {
    this shouldBe nonNegative()
    return this
}

fun nonNegative() = object : Matcher<Long> {
    override fun test(value: Long) = MatcherResult(
        value >= 0,
        { "$value should be >= 0" },
        { "$value should not be >= 0" }
    )
}