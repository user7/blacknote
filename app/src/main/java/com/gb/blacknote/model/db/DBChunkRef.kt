package com.gb.blacknote.model.db

import java.util.*

class DBChunkRef(
    val chunkId: UUID,
    val keyId: UUID,
    val iv: ByteArray,
)