package com.gb.blacknote

import com.gb.blacknote.db.protobuf.SNote
import com.gb.blacknote.db.protobuf.SVariant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random
import kotlin.test.assertContentEquals

@ExperimentalSerializationApi
class CryptoTests {

    @Test
    fun testGenerateRoundtripAESKey() {
        val key = KeyGenerator.getInstance("AES/GCM/NoPadding").generateKey()
        val enc = key.encoded
        println("encoded key: ${hex(enc)}")
        val key2 = unpackKey(enc)
        val enc2 = key2.encoded
        assertContentEquals(enc, enc2)
    }

    @Test
    fun testBlobRoundtrip() {
        val r = Random(12345)
        val data = r.nextBytes(100)
        val key = unpackKey(r.nextBytes(16))
        val iv = r.nextBytes(16)
        val encrypted = encrypt(data, key, iv)
        val decrypted = decrypt(encrypted, key, iv)
        assertContentEquals(data, decrypted)
    }

    private fun unpackKey(bytes: ByteArray): SecretKey = SecretKeySpec(bytes, "AES")

    @Test
    fun testChunkRoundtrip() {
        val iv = bin("01 34 29 67  A7 F5 2D CE  B8 21 83 57  49 A7 D4 66")
        val key = unpackKey(bin("01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16"))
        val obj = SVariant(
            uuid = bin("00 1D"),
            note = SNote(
                title = "a note",
                comment = "extended comment"
            ),
        )
        val encoded = chunkEncode(
            obj = obj,
            key = key,
            iv = iv,
        )
        val decoded = chunkDecode(encoded, key, iv)
        assertThat(decoded).isEqualToComparingFieldByFieldRecursively(obj)
    }

    private fun chunkEncode(
        obj: SVariant,
        key: SecretKey,
        iv: ByteArray,
    ): ByteArray {
        return encrypt(ProtoBuf.encodeToByteArray(obj), key, iv)
    }

    private fun chunkDecode(
        ciphertext: ByteArray,
        key: SecretKey,
        iv: ByteArray,
    ): SVariant {
        val data = decrypt(ciphertext, key, iv)
        return unpackValue<SVariant>(data)
    }

    private inline fun <reified T> unpackValue(bytes: ByteArray): T =
        ProtoBuf.decodeFromByteArray<T>(bytes)

    private fun hex(bytes: ByteArray): String =
        bytes.joinToString(" ") { "%02X".format(it) }

    private fun checkThrow(b: Boolean, msg: () -> String) {
        if (!b)
            throw IllegalArgumentException(msg())
    }

    private fun bin(hex: String) =
        hex.replace(" ", "").chunked(2)
            .map { it.toInt(16).toByte() }.toByteArray()

    private fun getCipher(mode: Int, key: SecretKey, iv: ByteArray): Cipher {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")!!
        cipher.init(mode, key, GCMParameterSpec(128, iv))
        return cipher
    }

    private fun encrypt(data: ByteArray, key: SecretKey, iv: ByteArray): ByteArray =
        getCipher(Cipher.ENCRYPT_MODE, key, iv).doFinal(data)

    private fun decrypt(ciphertext: ByteArray, key: SecretKey, iv: ByteArray): ByteArray =
        getCipher(Cipher.DECRYPT_MODE, key, iv).doFinal(ciphertext)
}