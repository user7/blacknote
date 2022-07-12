package com.gb.blacknote.storage.sqlite

import android.content.Context
import androidx.room.Room
import com.gb.blacknote.model.Model
import com.gb.blacknote.model.db.DBHeader
import com.gb.blacknote.model.db.DBNode
import com.gb.blacknote.storage.protobuf.SDBHeader
import com.gb.blacknote.storage.sqlite.room.AppDatabase
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.util.UUID
import javax.crypto.SecretKey

class SQLiteDatabase(context: Context) : Model.Database {

    val db = Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()
    var header_: DBHeader? = null

    override fun load() {
        val encodedHeader = db.headerDao().getLatestHeader()
        if (encodedHeader == null) {
            // database is empty, initialize it
            println("db is empty, initializing")
            TODO()
        }
        header_ = DBHeader(ProtoBuf.decodeFromByteArray<SDBHeader>(encodedHeader.data))
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    override fun getHeader(): DBHeader = header_!!

    override fun getItem(id: UUID): DBNode {
        TODO("Not yet implemented")
    }

    override fun loadItem(id: UUID, key: SecretKey) {
        TODO("Not yet implemented")
    }

    override fun observe(observer: Model.DatabaseObserver) {
        TODO("Not yet implemented")
    }
}