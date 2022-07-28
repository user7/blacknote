@file:OptIn(ExperimentalSerializationApi::class)

package com.gb.blacknote.db.sqlite

import android.content.Context
import androidx.room.Room
import com.gb.blacknote.db.protobuf.SChunkRef
import com.gb.blacknote.db.protobuf.SKey
import com.gb.blacknote.db.protobuf.StorageEncoder
import com.gb.blacknote.model.Model
import com.gb.blacknote.db.sqlite.room.AppDatabase
import com.gb.blacknote.db.sqlite.room.HeaderEntity
import com.gb.blacknote.model.db.*
import kotlinx.serialization.ExperimentalSerializationApi
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.Instant
import java.util.UUID

class DatabaseImpl(context: Context, private val storageEncoder: StorageEncoder) : Model.Database {

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()
    private var _header: DBHeader? = null
    private val observers: ArrayList<Model.DatabaseObserver> = arrayListOf()
    private val itemsToSave: HashSet<DBNode> = hashSetOf()
    private val itemsToLoad: HashSet<DBNode> = hashSetOf()
    private val itemsLoaded: HashSet<DBNode> = hashSetOf()

    override fun load() {
        val encodedHeader = db.headerDao().getLatestHeader()
        _header = encodedHeader?.let { unpackDBHeader(it) }
        notifyAll { it.onHeaderLoaded(_header) }
    }

    private fun unpackDBHeader(
        dbEntity: HeaderEntity,
    ): DBHeader {
        val sh = storageEncoder.decodeHeader(dbEntity.data)
        val keys = sh.keys.map { unpackKeyInfo(it) }
        val header = DBHeader(
            id = dbEntity.id,
            timestamp = Instant.ofEpochMilli(dbEntity.epochMillis),
            encryptedKeyInfos = keys,
            rootNodeRef = unpackDBChunkRef(sh.rootNode),
        )
        header.encryptedKeyInfos.forEach {
            if (it.derivationSalt == null) {
                header.activeKeys[it.keyId] =
                    ActiveKey(storageEncoder.unpackKey(it.encryptedKey))
            }
        }
        return header
    }

    private fun unpackDBChunkRef(ref: SChunkRef): DBChunkRef =
        DBChunkRef(
            chunkId = unpackUUID(ref.chunkId),
            keyId = unpackUUID(ref.keyId),
            iv = ref.iv
        )

    private fun unpackKeyInfo(sKey: SKey): KeyInfo =
        KeyInfo(
            keyId = unpackUUID(sKey.keyId),
            encryptedKey = sKey.keyBytes,
            derivationSalt = sKey.passSalt,
        )

    private fun unpackUUID(bytes: ByteArray): UUID =
        wrapBE(bytes).let {
            val msb = it.long
            val lsb = it.long
            UUID(msb, lsb)
        }

    private fun packUUID(uuid: UUID): ByteArray = ByteArray(16).apply {
        wrapBE(this).putLong(uuid.mostSignificantBits).putLong(uuid.leastSignificantBits)
    }

    private fun wrapBE(bytes: ByteArray) = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)

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

    override fun loadItem(ref: DBChunkRef, activeKey: ActiveKey) {
        TODO("Not yet implemented")
    }

    override fun addItem(id: UUID, node: DBNode) {
        TODO("Not yet implemented")
    }

    override fun observe(observer: Model.DatabaseObserver) {
        observers.add(observer)
    }
}

