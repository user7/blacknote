package com.gb.blacknote.model.db

import com.gb.blacknote.storage.protobuf.SDBHeader
import javax.crypto.SecretKey

class DBHeader(val sHeader: SDBHeader)
{
    val keys: Map<Long, SecretKey> = mapOf()
    val root: DBNode = DBNodePending(sHeader.rootNode)

    init {
        TODO("decrypt keys")
    }
}
