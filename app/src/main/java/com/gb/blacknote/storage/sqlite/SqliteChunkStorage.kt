package com.gb.blacknote.storage.sqlite

import android.content.Context
import androidx.room.Room
import com.gb.blacknote.storage.ChunkStorage
import com.gb.blacknote.storage.SplinterKey
import com.gb.blacknote.storage.protobuf.SDBHeader
import com.gb.blacknote.storage.protobuf.SVariantNode
import com.gb.blacknote.storage.sqlite.room.AppDatabase
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class SqliteChunkStorage(context: Context) : ChunkStorage {

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()

    override suspend fun loadHeader(): SDBHeader {
        TODO()
//        val query = db.headerDao().getHeader()
//        if (query.isEmpty()) {
//
//            val keygen = KeyGenerator.getInstance("AES")
//            keygen.init(256)
//            val key = keygen.generateKey()
//            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
//            cipher.init(Cipher.ENCRYPT_MODE, key)
//            val ciphertext: ByteArray = cipher.doFinal(plaintext)
//            val iv: ByteArray = cipher.iv
//
//            val rootFolder = SVariantNode(
//                UUID.randomUUID(),
//
//            )
//
//            val newSDBHeader = SDBHeader(
//
//            )
//            db.headerDao().replaceHeader()
//        }
    }

    override suspend fun saveHeader(data: SDBHeader) {
        TODO("Not yet implemented")
    }

    override suspend fun loadChunk(id: UUID, splinterKey: SplinterKey): SVariantNode {
        TODO("Not yet implemented")
    }

    override suspend fun saveChunk(id: UUID, splinterKey: SplinterKey, data: SVariantNode) {
        TODO("Not yet implemented")
    }
}