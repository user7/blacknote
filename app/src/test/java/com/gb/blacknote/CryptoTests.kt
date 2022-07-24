package com.gb.blacknote

import com.gb.blacknote.db.protobuf.SChunkHeader
import com.gb.blacknote.db.protobuf.SVariant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.junit.Test
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

@ExperimentalSerializationApi
class CryptoTests {

    @Test
    fun testByteBuffer() {

    }

    @Test
    fun testByteBufferSlice() {
        val data = bin("01 01 00 02 33 44 55 66 77 88 99")
        val buf = wrapBE(data)
        assertEquals(257, buf.short)
        assertEquals(0, buf.get())
        val bytes5 = ByteArray(5)
        buf.get(bytes5)
        assertContentEquals(bin("02 33 44 55 66"), bytes5)
    }

    fun applyCipher(data: ByteArray, key: SecretKey, mode: Int): ByteArray =
        Cipher.getInstance("AES/CBC/PKCS5PADDING")!!.let {
            it.init(mode, key)
            it.doFinal(data)
        }

    fun decrypt(bytes: ByteArray, key: SecretKey) = applyCipher(bytes, key, Cipher.DECRYPT_MODE)
    fun encrypt(bytes: ByteArray, key: SecretKey) = applyCipher(bytes, key, Cipher.ENCRYPT_MODE)

    private val packedExtra = Int.SIZE_BYTES + 16

    fun chunkEncode(
        obj: SVariant,
        salt: ByteArray,
        key: SecretKey,
    ): ByteArray {
        val data = ProtoBuf.encodeToByteArray(obj)
        val header = ProtoBuf.encodeToByteArray(SChunkHeader(
            salt = salt,
            dataLength = data.size,
            dataHash = md5(data),
        ))
        val sizeTagSize = packedULongLength(header.size.toULong())
        val plaintext = ByteArray(sizeTagSize + header.size + data.size)
        wrapBE(plaintext).apply {
            putPackedULong(this, header.size.toULong())
            put(header)
            put(data)
        }
        return encrypt(plaintext, key)
    }

    private fun md5(bytes: ByteArray) = MessageDigest.getInstance("MD5").digest(bytes)

    fun chunkDecode(
        ciphertext: ByteArray,
        key: SecretKey,
    ): SVariant {
        val data = try {
            val plaintext = decrypt(ciphertext, key)
            val buf = wrapBE(plaintext)
            val header = unpackValue<SChunkHeader>(bytes(buf, getPackedULong(buf).toInt()))
            val tmp = ByteArray(header.dataLength)
            buf.get(tmp)
            checkThrow(!md5(tmp).contentEquals(header.dataHash)) { "bad checksum" }
            tmp
        } catch(e: Exception) {
            throw IllegalArgumentException("wrong key or damaged data: $e")
        }

        // up to now exception means bad key, further exception means serialization error
        return unpackValue<SVariant>(data)
    }

    private fun bytes(buf: ByteBuffer, length: Int): ByteArray {
        val bytes = ByteArray(length)
        buf.get(bytes)
        return bytes
    }

    private inline fun <reified T> unpackValue(buf: ByteBuffer, size: Int): T {
        val bytes = ByteArray(size)
        buf.get(bytes)
        return ProtoBuf.decodeFromByteArray<T>(bytes)
    }

    private inline fun <reified T> unpackValue(bytes: ByteArray): T =
        ProtoBuf.decodeFromByteArray<T>(bytes)


    private fun getPackedULong(buf: ByteBuffer): ULong {
        var v: ULong = 0u
        while (true) {
            val byte = buf.get()
            if (byte >= 0)
                return v + byte.toULong()
            v = (v + (-(byte + 1)).toULong()) * 128u
        }
    }

    private fun putPackedULong(buf: ByteBuffer, value: ULong) {
        var v = value
        while (true) {
            if (v <= 127u) {
                buf.put(v.toByte())
                return
            }
            buf.put((-((v % 128u).toLong() + 1)).toByte())
            v /= 128u
        }
    }

    private fun packedULongLength(value: ULong): Int {
        var v = value
        var n = 1
        while (true) {
            if (v <= 127u)
                return n
            v /= 128u
            n++
        }
    }

    private fun checkThrow(b: Boolean, msg: () -> String) {
        if (!b)
            throw IllegalArgumentException(msg())
    }

    private fun wrapBE(bytes: ByteArray) = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)

    private fun bin(hex: String) =
        hex.replace(" ", "").chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}