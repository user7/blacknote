package com.gb.blacknote.model.db

import java.time.Instant
import java.util.UUID

class DBHeader(
    val id: UUID,
    val timestamp: Instant,
    val lockedKeys: List<KeyLocked>,
    rootNodeRef: DBChunkRef,
)
{
    val unlockedKeys: Map<UUID, KeyUnlocked> = mapOf()
    var root: DBNode = DBNodePending(rootNodeRef)
}