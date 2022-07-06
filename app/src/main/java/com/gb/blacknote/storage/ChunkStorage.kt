package com.gb.blacknote.storage

import com.gb.blacknote.storage.protobuf.SDBHeader
import com.gb.blacknote.storage.protobuf.SVariantNode
import java.util.UUID

interface ChunkStorage {

    fun loadHeader(): SDBHeader

    fun saveHeader(data: SDBHeader)

    fun loadChunk(id: UUID, splinterKey: SplinterKey): SVariantNode

    fun saveChunk(id: UUID, splinterKey: SplinterKey, data: SVariantNode)
}