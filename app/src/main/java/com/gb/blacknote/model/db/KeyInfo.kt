package com.gb.blacknote.model.db

import java.util.*

class KeyInfo(
    val keyId: UUID,
    val encryptedKey: ByteArray,
    val derivationSalt: ByteArray?,
)