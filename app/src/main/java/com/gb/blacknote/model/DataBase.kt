package com.gb.blacknote.model

import com.gb.blacknote.storage.ChunkStorage
import com.gb.blacknote.storage.protobuf.SDBHeader

class DataBase(val Storage: ChunkStorage) {

    var _header : MDBHeader? = null
    val isLoaded: Boolean = _header == null

    fun load() {

    }

    fun save() {

    }

    fun getMdb() : MDBHeader = _header!!

}