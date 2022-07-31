package com.gb.blacknote.model.db

import java.util.*

class StoredKey(
    val keyId: UUID,
    val encryptedKey: ByteArray,
    val passDerivationSalt: ByteArray,
)