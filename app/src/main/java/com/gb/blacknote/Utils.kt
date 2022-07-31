package com.gb.blacknote

fun hex(bytes: ByteArray): String = bytes.joinToString(" ") { "%02X".format(it) }

fun bin(hex: String) =
    hex.replace(" ", "").chunked(2)
        .map { it.toInt(16).toByte() }.toByteArray()

fun emptyNull(s: String): String? = s.ifEmpty { null }
fun emptyNull(b: ByteArray): ByteArray? = if (b.isEmpty()) null else b

fun nullEmpty(s: String?) = s ?: ""
fun nullEmpty(b: ByteArray?): ByteArray = b ?: ByteArray(0)
