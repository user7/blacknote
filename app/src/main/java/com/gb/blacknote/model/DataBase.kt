package com.gb.blacknote.model

import com.gb.blacknote.storage.ChunkStorage

class DataBase(val storage: ChunkStorage) {

    var _header : MDBHeader? = null
    val isLoaded: Boolean = _header == null

    fun load() {
        TODO()
    }

    fun save() {
        TODO()
    }

    fun getMdb() : MDBHeader = _header!!
}