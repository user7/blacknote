@file:OptIn(ExperimentalSerializationApi::class)

package com.gb.blacknote.db

import android.content.Context
import androidx.room.Room
import com.gb.blacknote.db.binary_format.BinaryFormatEncoder
import com.gb.blacknote.model.Model
import com.gb.blacknote.db.room.AppDatabase
import com.gb.blacknote.model.db.*
import com.gb.blacknote.model.db.nodes.Node
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.UUID

class DatabaseImpl(
    context: Context,
    private val encoder: BinaryFormatEncoder,
) : Model.Database {

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()
    private var header: DatabaseHeader? = null
    private val observers: ArrayList<Model.DatabaseObserver> = arrayListOf()

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun loadHeader() {
        val headerEntity = db.headerDao().getLatestHeader()
        header = headerEntity?.let { encoder.decodeHeader(it) }
        notifyAll { it.onHeaderLoaded(header) }
    }

    private fun notifyAll(callback: (Model.DatabaseObserver) -> Unit) {
        observers.forEach { callback(it) }
    }

    override fun getHeader(): DatabaseHeader? = header

    override fun addHeader(header: DatabaseHeader) {
        TODO("Not yet implemented")
    }

    override fun getNode(id: UUID): Node {
        TODO("Not yet implemented")
    }

    override fun loadNode(id: UUID) {
        TODO("Not yet implemented")
    }

    override fun addItem(id: UUID, node: Node) {
        TODO("Not yet implemented")
    }

    override fun observe(observer: Model.DatabaseObserver) {
        observers.add(observer)
    }
}

