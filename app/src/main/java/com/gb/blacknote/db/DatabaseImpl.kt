@file:OptIn(ExperimentalSerializationApi::class)

package com.gb.blacknote.db

import android.content.Context
import androidx.room.Room
import com.gb.blacknote.db.binary_format.BinaryFormatEncoder
import com.gb.blacknote.model.Model
import com.gb.blacknote.db.room.AppDatabase
import com.gb.blacknote.model.db.*
import com.gb.blacknote.model.db.nodes.BadNode
import com.gb.blacknote.model.db.nodes.Node
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.lang.Exception
import java.util.UUID

class DatabaseImpl(
    context: Context,
    private val encoder: BinaryFormatEncoder,
) : Model.Database {

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()
    private var header: DatabaseHeader? = null
    private val observers: ArrayList<Model.DatabaseObserver> = arrayListOf()
    private val nodes: HashMap<UUID, Node> = hashMapOf()

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val mainErrorHandler = CoroutineExceptionHandler { _, e -> onCoroutineMainError(e) }

    private fun onCoroutineMainError(e: Throwable) {
        notifyAll { it.onUnexpectedError(e.toString()) }
    }

    override fun getHeader(): DatabaseHeader? = header

    override fun loadHeader() {
        ioScope.launch {
            val headerEntity = db.headerDao().getLatestHeader()
            updateHeaderNotifyAll(headerEntity?.let { encoder.decodeHeader(it) })
        }
    }

    private fun updateHeaderNotifyAll(newHeader: DatabaseHeader?) {
        postMain(action = { header = newHeader }, notify = { it.onHeaderLoaded(newHeader) })
    }

    private fun postMain(action: (() -> Unit)? = null, notify: (Model.DatabaseObserver) -> Unit) {
        mainScope.launch(mainErrorHandler) {
            action?.let { action() }
            observers.forEach { notify(it) }
        }
    }

    override fun addHeader(newHeader: DatabaseHeader) {
        ioScope.launch {
            try {
                db.headerDao().replaceHeader(encoder.encodeHeader(newHeader))
                updateHeaderNotifyAll(newHeader)
            } catch (e: Exception) {
                postMain(notify = { it.onHeaderAddFailed(newHeader.headerId, e.toString()) })
            }
        }
    }

    override fun getNode(chunkId: UUID): Node? = nodes[chunkId]

    override fun loadNode(chunkId: UUID, keyIv: KeyIv?) {
        ioScope.launch {
            try {
                val entity = db.chunkDao().getChunk(chunkId)
                if (entity == null) {
                    postMain(action = {
                                      nodes[chunkId](BadNode(chunkId, "Node not found, shredded?"))
                    },
                            notify = {
                        it.onNodeLoaded(BadNode(chunkId, "Node not found, shredded?"))
                    })
                } else {
                }

                entity?.let {
                    encoder.decodeChunk(chunkEntity = it, keyIv = keyIv)
                }
                encoder.decodeChunk(chunkEntity = entity)
            } catch (e: Exception) {

            }
        }
    }

    override fun addItem(id: UUID, node: Node) {
        TODO("Not yet implemented")
    }

    override fun observe(observer: Model.DatabaseObserver) {
        observers.add(observer)
    }
}

