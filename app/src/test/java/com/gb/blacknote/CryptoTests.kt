package com.gb.blacknote

import com.gb.blacknote.db.protobuf.ProtobufEncoder
import com.gb.blacknote.db.room.ChunkEntity
import com.gb.blacknote.model.db.nodes.NoteNode
import kotlinx.serialization.ExperimentalSerializationApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*
import kotlin.test.assertContentEquals

@ExperimentalSerializationApi
class CryptoTests {

    val encoder = ProtobufEncoder()

    @Test
    fun testGenerateRoundtripAESKey() {
        val key = encoder.generateKey()
        val enc = key.encoded
        val key2 = encoder.decodeKey(enc)
        val enc2 = key2.encoded
        assertContentEquals(enc, enc2)
    }

    @Test
    fun testChunkRoundtrip() {
        val iv = encoder.generateIv()
        val key = encoder.generateKey()
        val chunkId = encoder.generateUUID()
        val nodeId = encoder.generateUUID()
        val node = NoteNode(nodeId = nodeId, title = "a note", comment = "extended comment")
        val keyIv = ProtobufEncoder.KeyIv(key, iv)
        val encoded = encoder.encodeChunk(chunkId = chunkId, node = node, keyIv = keyIv)
        val decoded = encoder.decodeChunk(chunkEntity = encoded, keyIv = keyIv)
        assertThat(decoded).isEqualToComparingFieldByFieldRecursively(node)
    }
}