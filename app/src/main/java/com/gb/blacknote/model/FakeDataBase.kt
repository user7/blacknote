package com.gb.blacknote.model

import com.gb.blacknote.storage.ChunkStorage
import java.util.*

class FakeDataBase(val storage: ChunkStorage) {

    private var _header : MDBHeader? = null

    fun load() {
        TODO()
    }

    fun save() {
        TODO()
    }

    fun getHeader() : MDBHeader? = _header

    fun getItem(id: UUID) : MNode = TODO()
}