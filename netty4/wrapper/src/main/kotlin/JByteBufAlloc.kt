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

import io.netty.buffer.ByteBufAllocator

public fun ByteBufAllocator.jBuffer(): JByteBuf = buffer().toJBytebuf()

public fun ByteBufAllocator.jBuffer(initialCapacity: Int): JByteBuf = buffer(initialCapacity).toJBytebuf()

public fun ByteBufAllocator.jBuffer(initialCapacity: Int, maxCapacity: Int): JByteBuf =
    buffer(initialCapacity, maxCapacity).toJBytebuf()

public fun ByteBufAllocator.jIoBuffer(): JByteBuf = ioBuffer().toJBytebuf()

public fun ByteBufAllocator.jIoBuffer(initialCapacity: Int): JByteBuf = ioBuffer(initialCapacity).toJBytebuf()

public fun ByteBufAllocator.jIoBuffer(initialCapacity: Int, maxCapacity: Int): JByteBuf =
    ioBuffer(initialCapacity, maxCapacity).toJBytebuf()

public fun ByteBufAllocator.jHeapBuffer(): JByteBuf = heapBuffer().toJBytebuf()

public fun ByteBufAllocator.jHeapBuffer(initialCapacity: Int): JByteBuf = heapBuffer(initialCapacity).toJBytebuf()

public fun ByteBufAllocator.jHeapBuffer(initialCapacity: Int, maxCapacity: Int): JByteBuf =
    heapBuffer(initialCapacity, maxCapacity).toJBytebuf()

public fun ByteBufAllocator.jDirectBuffer(): JByteBuf = directBuffer().toJBytebuf()

public fun ByteBufAllocator.jDirectBuffer(initialCapacity: Int): JByteBuf = directBuffer(initialCapacity).toJBytebuf()

public fun ByteBufAllocator.jDirectBuffer(initialCapacity: Int, maxCapacity: Int): JByteBuf =
    directBuffer(initialCapacity, maxCapacity).toJBytebuf()