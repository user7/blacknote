package com.gb.blacknote.model.db

import javax.crypto.SecretKey

class KeyIv(
    val key: SecretKey,
    val iv: ByteArray,
)