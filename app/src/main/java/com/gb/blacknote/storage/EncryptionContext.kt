package com.gb.blacknote.storage

import javax.crypto.SecretKey

data class SplinterKey(
    val splinter: Long,
    val key: SecretKey,
)
