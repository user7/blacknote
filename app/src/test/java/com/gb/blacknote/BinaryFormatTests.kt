package com.gb.blacknote

import com.gb.blacknote.db.binary_format.BinaryFormatEncoder
import com.gb.blacknote.model.db.ChunkRef
import com.gb.blacknote.model.db.DatabaseHeader
import com.gb.blacknote.model.db.nodes.NoteNode
import kotlinx.serialization.ExperimentalSerializationApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.test.assertContentEquals

@ExperimentalSerializationApi
class BinaryFormatTests {

    private val encoder = BinaryFormatEncoder()

    @Test
    fun testGenerateRoundtripAESKey() {
        val key = encoder.generateKey()
        val enc = key.encoded
        val key2 = encoder.decodeKey(enc)
        val enc2 = key2.encoded
        assertContentEquals(enc, enc2)
    }

    @Test
    fun testHeaderRoundtrip() {
        val headerId = encoder.generateUUID()
        val keyId = encoder.generateUUID()
        val rootChunkId = encoder.generateUUID()
        val rootIv = encoder.generateIv()
        val header = DatabaseHeader(
            headerId = headerId,
            timestamp = encoder.decodeTimestamp(123),
            storedKeys = arrayListOf(),
            zz = ChunkRef(chunkId = rootChunkId, iv = rootIv, keyId = keyId)
        )
        val encoded = encoder.encodeHeader(header)
        val decoded = encoder.decodeHeader(encoded)
        assertThat(decoded).isEqualToComparingFieldByFieldRecursively(header)
    }

    @Test
    fun testChunkRoundtrip() {
        val iv = encoder.generateIv()
        val key = encoder.generateKey()
        val chunkId = encoder.generateUUID()
        val nodeId = encoder.generateUUID()
        val node = NoteNode(nodeId = nodeId, title = "a note", comment = "extended comment")
        val keyIv = BinaryFormatEncoder.KeyIv(key, iv)
        val encoded = encoder.encodeChunk(chunkId = chunkId, node = node, keyIv = keyIv)
        val decoded = encoder.decodeChunk(chunkEntity = encoded, keyIv = keyIv)
        assertThat(decoded).isEqualToComparingFieldByFieldRecursively(node)
    }
}