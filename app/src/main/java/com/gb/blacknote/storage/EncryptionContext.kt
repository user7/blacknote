package com.gb.blacknote.storage

import javax.crypto.SecretKey

data class EncryptionContext(
    val splinter: Long,
    val key: SecretKey,
)
