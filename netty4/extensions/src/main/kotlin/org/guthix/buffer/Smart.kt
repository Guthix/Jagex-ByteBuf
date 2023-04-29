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
package org.guthix.buffer

public object Smart {
    public const val MAX_BYTE_VALUE: Int = 63
    public const val MIN_BYTE_VALUE: Int = -64
    public const val BYTE_MOD: Int = MAX_BYTE_VALUE + 1
    public const val MAX_SHORT_VALUE: Int = 16383
    public const val MIN_SHORT_VALUE: Int = -16384
    public const val SHORT_MOD: Int = MAX_SHORT_VALUE + 1
    public const val MAX_INT_VALUE: Int = 1_073_741_823
    public const val MIN_INT_VALUE: Int = -1_073_741_824
    public const val INT_MOD: Int = MAX_INT_VALUE + 1
}

public object USmart {
    public const val MAX_BYTE_VALUE: Int = Byte.MAX_VALUE.toInt()
    public const val MIN_BYTE_VALUE: Int = 0
    public const val MAX_SHORT_VALUE: Int = Short.MAX_VALUE.toInt()
    public const val MIN_SHORT_VALUE: Int = 0
    public const val MAX_INT_VALUE: Int = Int.MAX_VALUE
    public const val MIN_INT_VALUE: Int = 0
}