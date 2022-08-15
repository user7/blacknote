package com.gb.blacknote.model

import androidx.lifecycle.LiveData
import com.gb.blacknote.model.db.DatabaseHeader
import com.gb.blacknote.model.db.nodes.Node
import java.util.*

class Model {

    interface DatabaseObserver {
        fun onHeaderLoaded(header: DatabaseHeader?)
        fun onHeaderLoadingFailed(headerId: UUID, error: String)
        fun onHeaderAddFailed(headerId: UUID, error: String)

        fun onNodeLoaded(node: Node)
        fun onNodeLoadingFailed(chunkId: UUID, error: String)
        fun onNodeAddFailed(chunkId: UUID, error: String)

        fun onUnexpectedError(error: String)
    }

    interface Database {

        fun getHeader(): DatabaseHeader?        // immediately get header or null
        fun loadHeader()                        // load latest header, notify observer
        fun addHeader(header: DatabaseHeader)   // add new header and save it

        fun getNode(id: UUID): Node?           // get item, may be DBNodePending
        fun loadNode(id: UUID)                // load node, notify observer
        fun addItem(id: UUID, node: Node)     // add new item and save it to db

        fun observe(observer: DatabaseObserver)
    }

    sealed class DbState {
        class Loading(val dbName: String) : DbState() {}
        class Broken(val error: Exception) : DbState() {}
        class Loaded(val database: Database) : DbState() {}
    }

    interface MainViewModel {
        fun dbState(): LiveData<DbState>
    }
}

typealias TimestampEpochMillis = Long