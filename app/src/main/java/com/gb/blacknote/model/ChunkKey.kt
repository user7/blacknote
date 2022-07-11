package com.gb.blacknote.model

import javax.crypto.SecretKey

class ChunkKey(
    val key: SecretKey,
    val header: ByteArray,
)
