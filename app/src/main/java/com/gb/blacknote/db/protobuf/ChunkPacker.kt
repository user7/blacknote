package com.gb.blacknote.db.protobuf

import com.gb.blacknote.model.db.*
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.lang.IllegalArgumentException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.Instant
import java.util.*

class ChunkPacker {

    fun unpackHeader(
        id: UUID,
        bytes: ByteArray,
        timestamp: Instant,
    ): DBHeader {
        val header = ProtoBuf.decodeFromByteArray<SDBHeader>(bytes)
        return DBHeader(
            id = id,
            timestamp = timestamp,
            lockedKeys = header.keys.map {
                KeyLocked(
                    keyId = byteArrayToUUID(it.keyId),
                    encryptedKey = it.keyBytes,
                    derivationSalt = it.passSalt,
                )
            },
            rootNodeRef = DBChunkRef(
                keyId = byteArrayToUUID(header.rootNode.keyId),
                chunkId = byteArrayToUUID(header.rootNode.chunkId),
                header = header.rootNode.header,
            )
        )
    }

    fun unpackChunk(
        ref: DBChunkRef,
        key: KeyUnlocked,
        bytes: ByteArray,
    ): DBNode {
        val decBytes = decrypt(key, bytes)
        val decBuf = wrapBuf(decBytes)
        val length = decBuf.long
        val digest = decBuf.long
        val nodeBytes = decBuf.slice()
        TODO("------")
        // val sNode = ProtoBuf.decodeFromByteArray<SVariantNode>()
    }

    private fun getInt(bytes: ByteArray, offset: Int): Int {
        TODO()
    }

    private fun getLong(bytes: ByteArray, offset: Int): Int {
        TODO()
    }

    fun decrypt(key: KeyUnlocked, bytes: ByteArray): ByteArray {
        TODO("decryption api")
    }

    fun packHeader() {
        TODO("something")
    }

    private fun byteArrayToUUID(bytes: ByteArray): UUID {
        val expSize = 2 * Long.SIZE_BYTES
        if (bytes.size != expSize) {
            val hex = bytesToHex(bytes)
            throw IllegalArgumentException("error unpacking UUID '$hex': expected $expSize bytes")
        }
        val buf = wrapBuf(bytes)
        val lsb = buf.long
        val msb = buf.long
        return UUID(msb, lsb)
    }

    private fun bytesToHex(bytes: ByteArray): String =
        bytes.joinToString(separator = "") { byte -> "%02x".format(byte) }

    private fun wrapBuf(bytes: ByteArray): ByteBuffer =
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
}