package com.gb.blacknote.model

import com.gb.blacknote.storage.ChunkStorage
import java.util.*
import javax.crypto.SecretKey

interface IDataBase {

    fun load()

    fun save()

    fun getHeader()

    fun getItem(id: UUID) : MNode

    fun loadItem(id: UUID, key: SecretKey)
}