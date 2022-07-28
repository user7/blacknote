package com.gb.blacknote.model.db

import java.time.Instant
import java.util.UUID

class DBHeader(
    val id: UUID,
    val timestamp: Instant,
    val encryptedKeyInfos: List<KeyInfo>,
    rootNodeRef: DBChunkRef,
)
{
    val activeKeys: MutableMap<UUID, ActiveKey> = hashMapOf()
    var root: DBNode = DBNodePending(rootNodeRef)
}