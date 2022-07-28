package com.gb.blacknote.model

import androidx.lifecycle.LiveData
import com.gb.blacknote.model.db.DBChunkRef
import com.gb.blacknote.model.db.DBHeader
import com.gb.blacknote.model.db.DBNode
import com.gb.blacknote.model.db.ActiveKey
import java.util.*

class Model {

    interface DatabaseObserver {
        fun onHeaderLoaded(header: DBHeader?)
        fun onNodeLoaded(node: DBNode)
        fun onNodeLoadingFailed(id: UUID)
    }

    interface Database {
        fun load()
        fun save()
        fun getHeader(): DBHeader?
        fun addHeader(header: DBHeader)
        fun getItem(id: UUID): DBNode
        fun loadItem(ref: DBChunkRef, activeKey: ActiveKey)
        fun addItem(id: UUID, node: DBNode)
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