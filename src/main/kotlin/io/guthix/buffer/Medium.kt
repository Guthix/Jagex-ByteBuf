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
package io.guthix.buffer

internal object Medium {
    const val SIZE_BYTES: Int = 3
    const val SIZE_BITS: Int = SIZE_BYTES * Byte.SIZE_BITS
    const val MAX_VALUE: Int = 8_388_607
    const val MIN_VALUE: Int = -8_388_608
}

internal object UMedium {
    const val SIZE_BYTES: Int = Medium.SIZE_BYTES
    const val SIZE_BITS: Int = Medium.SIZE_BITS
    const val MAX_VALUE: UInt = 16_777_215u
    const val MIN_VALUE: UInt = 0u
}