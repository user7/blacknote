package com.gb.blacknote.storage.sqlite

import android.content.Context
import androidx.room.Room
import com.gb.blacknote.storage.ChunkStorage
import com.gb.blacknote.storage.protobuf.SDBHeader
import com.gb.blacknote.storage.protobuf.SVariantNode
import com.gb.blacknote.storage.sqlite.room.AppDatabase
import java.util.*
import javax.crypto.SecretKey

class SqliteChunkStorage(context: Context) : ChunkStorage {

    val db = Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()

    override fun loadHeader(): SDBHeader {
        TODO("Not yet implemented")
    }

    override fun saveHeader(data: SDBHeader) {
        TODO("Not yet implemented")
    }

    override fun loadChunk(id: UUID, salt: Long, key: SecretKey): SVariantNode {
        TODO("Not yet implemented")
    }

    override fun saveChunk(id: UUID, salt: Long, key: SecretKey, data: SVariantNode) {
        TODO("Not yet implemented")
    }
}