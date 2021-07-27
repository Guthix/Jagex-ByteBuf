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

internal object ShortSmart {
    const val MAX_BYTE_VALUE: Int = 63
    const val MIN_BYTE_VALUE: Int = -64
    const val MAX_SHORT_VALUE: Int = 16383
    const val MIN_SHORT_VALUE: Int = -16384
    const val MAX_VALUE: Short = 16383
    const val MIN_VALUE: Short = -16384
}

internal object UShortSmart {
    const val MAX_BYTE_VALUE: Int = 127
    const val MIN_BYTE_VALUE: Int = 0
    const val MAX_SHORT_VALUE: Int = 32767
    const val MIN_SHORT_VALUE: Int = 0
    const val MAX_VALUE: UShort = 32767u
    const val MIN_VALUE: UShort = 0u
}