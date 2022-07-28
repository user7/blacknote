@file:OptIn(ExperimentalSerializationApi::class)

package com.gb.blacknote.db.protobuf

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


object StorageEncoder {

    class KeyData(
        val key: SecretKey,
        val iv: ByteArray,
    )

    private const val algo = "AES/GCM/NoPadding"
    private val rng = SecureRandom()

    fun generateKey(): SecretKey = KeyGenerator.getInstance(algo).apply { init(rng) }.generateKey()
    fun unpackKey(bytes: ByteArray): SecretKey = SecretKeySpec(bytes, "AES")

    fun generateIv(): ByteArray = ByteArray(16).apply { rng.nextBytes(this) }

    fun encodeChunk(
        chunk: SChunk,
        keyData: KeyData?,
    ): ByteArray = encodeObject(chunk, keyData)

    fun decodeChunk(
        ciphertext: ByteArray,
        keyData: KeyData?,
    ): SChunk = decodeObject(ciphertext, keyData)

    fun encodeHeader(header: SDBHeader): ByteArray = encodeObject(header, null)
    fun decodeHeader(data: ByteArray): SDBHeader = decodeObject(data, null)

    private inline fun <reified T> encodeObject(obj: T, keyData: KeyData?): ByteArray {
        val plaintext = ProtoBuf.encodeToByteArray(obj)
        return applyCipher(Cipher.ENCRYPT_MODE, plaintext, keyData)
    }

    private inline fun <reified T> decodeObject(ciphertext: ByteArray, keyData: KeyData?): T {
        val plaintext = applyCipher(Cipher.DECRYPT_MODE, ciphertext, keyData)
        return ProtoBuf.decodeFromByteArray(plaintext)
    }

    private fun applyCipher(
        mode: Int,
        data: ByteArray,
        keyData: KeyData?
    ): ByteArray {
        keyData ?: return data
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")!!
        cipher.init(mode, keyData.key, GCMParameterSpec(128, keyData.iv))
        return cipher.doFinal(data)
    }
}