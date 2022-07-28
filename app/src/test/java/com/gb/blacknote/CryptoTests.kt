package com.gb.blacknote

import com.gb.blacknote.db.protobuf.SNote
import com.gb.blacknote.db.protobuf.SChunk
import com.gb.blacknote.db.protobuf.StorageEncoder
import kotlinx.serialization.ExperimentalSerializationApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
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
        val random16 = bin("01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16")
        val iv = random16
        val key = StorageEncoder.unpackKey(random16)
        val chunk = SChunk(
            uuid = bin("1D 2E"),
            note = SNote(
                title = "a note",
                comment = "extended comment"
            ),
        )
        val keyData = StorageEncoder.KeyData(key, iv)
        val encoded = StorageEncoder.encodeChunk(chunk = chunk, keyData = keyData)
        val decoded = StorageEncoder.decodeChunk(ciphertext = encoded, keyData = keyData)
        assertThat(decoded).isEqualToComparingFieldByFieldRecursively(chunk)
    }

    private fun hex(bytes: ByteArray): String =
        bytes.joinToString(" ") { "%02X".format(it) }

    private fun bin(hex: String) =
        hex.replace(" ", "").chunked(2)
            .map { it.toInt(16).toByte() }.toByteArray()
}