package io.guthix.buffer

import java.nio.charset.Charset

private val cp1252 = Charset.availableCharsets()["windows-1252"] ?: throw IllegalStateException(
    "Could not find CP1252 character set"
)

private val cesu8 = Charset.availableCharsets()["CESU-8"] ?: throw IllegalStateException(
    "Could not find CESU-8 character set"
)