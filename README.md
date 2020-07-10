# Jagex-ByteBuf
[![Build Status](https://github.com/guthix/jagex-bytebuf/workflows/Build/badge.svg)](https://github.com/guthix/Jagex-ByteBuf/actions?workflow=Build)
[![License](https://img.shields.io/github/license/guthix/Jagex-ByteBuf)](https://github.com/guthix/Jagex-ByteBuf/blob/master/LICENSE)
[![Discord](https://img.shields.io/discord/538667877180637184?color=%237289da&logo=discord)](https://discord.gg/AFyGxNp)

Jagex-ByteBuf is a collection of Netty ByteBuf extensions for reading 
and writing RuneTek engine data type encoding. Jagex-ByteBuf uses Kotlin
extension functions to add RuneTek engine ByteBuf read and write methods
to the Netty ByteBuf interface. It also adds a bit buffer implementation that
provides reading and writing with bit granularity.