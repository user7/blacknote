package com.gb.blacknote.model.db

import java.util.*

class LockedKey(
    val keyId: UUID,
    val keyBytes: ByteArray,
    val derivationSalt: ByteArray,
)
