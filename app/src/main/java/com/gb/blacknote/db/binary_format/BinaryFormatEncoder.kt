@file:OptIn(ExperimentalSerializationApi::class)

package com.gb.blacknote.db.binary_format

import com.gb.blacknote.db.binary_format.proto.*
import com.gb.blacknote.db.room.ChunkEntity
import com.gb.blacknote.db.room.HeaderEntity
import com.gb.blacknote.emptyNull
import com.gb.blacknote.model.db.*
import com.gb.blacknote.model.db.nodes.*
import com.gb.blacknote.nullEmpty
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.lang.IllegalArgumentException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.time.Instant
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


class BinaryFormatEncoder {

    class KeyIv(
        val key: SecretKey,
        val iv: ByteArray,
    )

    companion object {
        const val ALGO = "AES/GCM/NoPadding"
        const val KEY_ALGO = "AES"
    }

    private val rng = SecureRandom()

    fun generateKey(): SecretKey =
        KeyGenerator.getInstance(KEY_ALGO).apply { init(rng) }.generateKey()

    fun generateIv(): ByteArray = ByteArray(16).apply { rng.nextBytes(this) }
    fun generateUUID(): UUID = UUID.randomUUID()
    fun decodeKey(bytes: ByteArray): SecretKey = SecretKeySpec(bytes, KEY_ALGO)

    private fun encodeKeyInfo(storedKey: StoredKey) = StoredKeyProto(
        keyId = encodeUUID(storedKey.keyId),
        keyBytes = storedKey.encryptedKey,
        passSalt = storedKey.passDerivationSalt,
    )

    fun decodeTimestamp(epochMs: Long): Instant = Instant.ofEpochMilli(epochMs)
    fun encodeTimestamp(timestamp: Instant): Long = timestamp.toEpochMilli()

    fun decodeHeader(headerEntity: HeaderEntity): DatabaseHeader {
        val headerProto = deserializeObject<DatabaseHeaderProto>(headerEntity.data, null)
        val keys = headerProto.keys.map { decodeStoredKey(it) }
        val header = DatabaseHeader(
            headerId = headerEntity.headerId,
            timestamp = decodeTimestamp(headerEntity.epochMs),
            storedKeys = keys,
            rootNodeRef = decodeChunkRef(headerProto.rootNode),
        )
        header.storedKeys.forEach {
            if (it.passDerivationSalt.isEmpty()) {
                header.activeKeys[it.keyId] =
                    ActiveKey(decodeKey(it.encryptedKey))
            }
        }
        return header
    }

    fun encodeHeader(header: DatabaseHeader): HeaderEntity {
        val keys = header.storedKeys.map { encodeKeyInfo(it) }
        val rootNode = encodeChunkRef(header.root.chunkRef)
        return HeaderEntity(
            headerId = header.headerId,
            data = serializeObject(
                DatabaseHeaderProto(
                    keys = keys,
                    rootNode = rootNode,
                )
            ),
            epochMs = encodeTimestamp(header.timestamp)
        )
    }

    private inline fun <reified T> serializeObject(
        obj: T,
        keyIv: KeyIv? = null
    ): ByteArray {
        val plaintext = ProtoBuf.encodeToByteArray(obj)
        return applyCipher(Cipher.ENCRYPT_MODE, plaintext, keyIv)
    }

    private inline fun <reified T> deserializeObject(
        ciphertext: ByteArray,
        keyIv: KeyIv? = null
    ): T {
        val plaintext = applyCipher(Cipher.DECRYPT_MODE, ciphertext, keyIv)
        return ProtoBuf.decodeFromByteArray(plaintext)
    }

    private fun applyCipher(
        mode: Int,
        data: ByteArray,
        keyIv: KeyIv?
    ): ByteArray {
        keyIv ?: return data
        val cipher = Cipher.getInstance(ALGO)!!
        cipher.init(mode, keyIv.key, GCMParameterSpec(128, keyIv.iv))
        return cipher.doFinal(data)
    }

    fun encodeChunk(
        chunkId: UUID,
        node: Node,
        keyIv: KeyIv?,
    ): ChunkEntity {
        val id = encodeUUID(node.nodeId)
        val proto: ChunkProto =
            when (node) {
                is FolderNode -> ChunkProto(nodeId = id, folder = encodeFolderNode(node))
                is FileNode -> ChunkProto(nodeId = id, file = encodeFileNode(node))
                is NoteNode -> ChunkProto(nodeId = id, note = encodeNoteNode(node))
                is UnsupportedNode ->
                    throw IllegalArgumentException("node of unsupported type can not be encoded")
            }
        return ChunkEntity(chunkId = chunkId, data = serializeObject(proto, keyIv))
    }

    private fun encodeFolderNode(folder: FolderNode) =
        FolderNodeProto(
            name = emptyNull(folder.name),
            items = folder.items.map { encodeChunkRef(it.chunkRef) },
        )

    private fun encodeNoteNode(note: NoteNode) =
        NoteNodeProto(
            title = emptyNull(note.title),
            comment = emptyNull(note.comment),
        )

    private fun encodeFileNode(note: FileNode) =
        FileNodeProto(
            name = emptyNull(note.name),
            contents = note.contents,
        )

    fun decodeChunk(
        chunkEntity: ChunkEntity,
        keyIv: KeyIv?,
    ): Node {
        val proto: ChunkProto = deserializeObject(chunkEntity.data, keyIv)
        val nodeId = decodeUUID(proto.nodeId)
        val node: Node =
            proto.folder?.let { decodeFolderNode(nodeId, it) } ?: proto.note?.let {
                decodeNoteNode(
                    nodeId,
                    it
                )
            } ?: proto.file?.let { decodeFileNode(nodeId, it) } ?: UnsupportedNode(nodeId)
        return node
    }

    private fun decodeFolderNode(nodeId: UUID, proto: FolderNodeProto) =
        FolderNode(
            nodeId = nodeId,
            name = nullEmpty(proto.name),
            items = ArrayList(proto.items.map { NodeHolder(decodeChunkRef(it)) }),
        )

    private fun decodeNoteNode(nodeId: UUID, proto: NoteNodeProto) =
        NoteNode(
            nodeId = nodeId,
            title = nullEmpty(proto.title),
            comment = nullEmpty(proto.comment),
        )

    private fun decodeFileNode(nodeId: UUID, proto: FileNodeProto) =
        FileNode(
            nodeId = nodeId,
            name = nullEmpty(proto.name),
            contents = nullEmpty(proto.contents),
        )


    private fun decodeChunkRef(proto: ChunkRefProto) = ChunkRef(
        chunkId = decodeUUID(proto.chunkId),
        keyId = decodeUUID(proto.keyId),
        iv = proto.iv
    )

    private fun encodeChunkRef(chunkRef: ChunkRef) = ChunkRefProto(
        chunkId = encodeUUID(chunkRef.chunkId),
        keyId = encodeUUID(chunkRef.keyId),
        iv = chunkRef.iv
    )

    private fun decodeStoredKey(proto: StoredKeyProto) = StoredKey(
        keyId = decodeUUID(proto.keyId),
        encryptedKey = proto.keyBytes,
        passDerivationSalt = proto.passSalt,
    )

    private fun encodeStoredKey(storedKey: StoredKey) = StoredKeyProto(
        keyId = encodeUUID(storedKey.keyId),
        keyBytes = storedKey.encryptedKey,
        passSalt = storedKey.passDerivationSalt,
    )

    private fun decodeUUID(bytes: ByteArray): UUID =
        wrapBufferBE(bytes).let {
            val msb = it.long
            val lsb = it.long
            UUID(msb, lsb)
        }

    private fun encodeUUID(uuid: UUID): ByteArray = ByteArray(16).apply {
        wrapBufferBE(this).putLong(uuid.mostSignificantBits).putLong(uuid.leastSignificantBits)
    }

    private fun wrapBufferBE(bytes: ByteArray) = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)
}