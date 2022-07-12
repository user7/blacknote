package com.gb.blacknote.storage

import com.gb.blacknote.model.MDBHeader
import com.gb.blacknote.model.MNode
import java.util.*
import javax.crypto.SecretKey

interface IDataBase {

    fun load()

    fun save()

    fun getHeader() : MDBHeader?

    fun getItem(id: UUID) : MNode

    fun loadItem(id: UUID, key: SecretKey)
}