package com.gb.blacknote.model

import androidx.lifecycle.LiveData
import com.gb.blacknote.model.db.DBHeader
import com.gb.blacknote.model.db.DBNode
import java.util.*
import javax.crypto.SecretKey

class Model {

    interface DatabaseObserver {
        fun onChanged(id: UUID)
    }

    interface Database {
        fun load()
        fun save()
        fun getHeader(): DBHeader
        fun getItem(id: UUID): DBNode
        fun loadItem(id: UUID, key: SecretKey)
        fun observe(observer: DatabaseObserver)
    }

    sealed class MainState {
        class Loading(val dbName: String) : MainState() {}
        class LoadingFailed(val error: Exception) : MainState() {}
        class DatabaseLoaded(val database: Database) : MainState() {}
    }

    interface MainViewModel {
        fun appState(): LiveData<MainState>
    }
}