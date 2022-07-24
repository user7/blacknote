package com.gb.blacknote.model.db

import java.util.*

class KeyLocked(
    val keyId: UUID,
    val encryptedKey: ByteArray,
    val derivationSalt: ByteArray,
)