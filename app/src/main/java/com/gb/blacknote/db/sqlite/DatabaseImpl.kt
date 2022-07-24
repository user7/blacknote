package com.gb.blacknote.db.sqlite

import android.content.Context
import androidx.room.Room
import com.gb.blacknote.db.protobuf.ChunkPacker
import com.gb.blacknote.model.Model
import com.gb.blacknote.db.sqlite.room.AppDatabase
import com.gb.blacknote.model.db.*
import java.time.Instant
import java.util.UUID

class DatabaseImpl(context: Context, private val packer: ChunkPacker) : Model.Database {

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()
    private var _header: DBHeader? = null
    private val observers: ArrayList<Model.DatabaseObserver> = arrayListOf()
    private val itemsToSave: HashSet<DBNode> = hashSetOf()
    private val itemsToLoad: HashSet<DBNode> = hashSetOf()
    private val itemsLoaded: HashSet<DBNode> = hashSetOf()

    override fun load() {
        val encodedHeader = db.headerDao().getLatestHeader()
        _header = encodedHeader?.let {
            packer.unpackHeader(
                encodedHeader.id,
                encodedHeader.data,
                Instant.ofEpochMilli(encodedHeader.epochMillis),
            )
        }
        notifyAll { it.onHeaderLoaded(_header) }
    }

    private fun notifyAll(callback: (Model.DatabaseObserver) -> Unit) {
        observers.forEach { callback(it) }
    }

    override fun save() {
        // wait for all pending requests
        TODO("Not yet implemented")
    }

    override fun getHeader(): DBHeader? = _header

    override fun addHeader(header: DBHeader) {
        TODO("Not yet implemented")
    }

    override fun getItem(id: UUID): DBNode {
        TODO("Not yet implemented")
    }

    override fun loadItem(ref: DBChunkRef, key: KeyUnlocked) {
        TODO("Not yet implemented")
    }

    override fun addItem(id: UUID, node: DBNode) {
        TODO("Not yet implemented")
    }

    override fun observe(observer: Model.DatabaseObserver) {
        observers.add(observer)
    }
}