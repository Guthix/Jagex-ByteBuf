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

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

public abstract class JByteToMessageDecoder : ByteToMessageDecoder() {
    public abstract fun decode(ctx: ChannelHandlerContext, inc: JByteBuf, out: MutableList<Any>)

    final override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        decode(ctx, `in`.toJBytebuf(), out)
    }

    public abstract fun decodeLast(ctx: ChannelHandlerContext, inc: JByteBuf, out: MutableList<Any>)

    final override fun decodeLast(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        decodeLast(ctx, `in`.toJBytebuf(), out)
    }
}