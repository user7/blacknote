package com.gb.blacknote.model.db

import java.time.Instant
import java.util.UUID

class DatabaseHeader(
    val headerId: UUID,
    val timestamp: Instant,
    val storedKeys: List<StoredKey>,
    var rootNodeRef: ChunkRef,
)
{
    val activeKeys: MutableMap<UUID, ActiveKey> = hashMapOf()
}