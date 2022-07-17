package com.gb.blacknote.model.db

import com.gb.blacknote.storage.protobuf.SDBHeader
import java.util.*
import javax.crypto.SecretKey

class DBHeader(
    lockedKeys: List<LockedKey>
)
{
    val storedKeys: List<EncryptedKey>
    val activeKeys: Map<UUID, ActiveKey> = mapOf()
    val root: DBNode = DBNodePending()

    init {
        TODO("decrypt keys")
    }
}
