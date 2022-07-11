package com.gb.blacknote.storage

import com.gb.blacknote.storage.protobuf.SDBHeader
import com.gb.blacknote.storage.protobuf.SVariantNode
import java.util.UUID

interface ChunkStorage {

    suspend fun loadHeader(): SDBHeader

    suspend fun saveHeader(data: SDBHeader)

    suspend fun loadChunk(id: UUID, splinterKey: SplinterKey): SVariantNode

    suspend fun saveChunk(id: UUID, splinterKey: SplinterKey, data: SVariantNode)
}