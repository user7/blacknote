package com.gb.blacknote.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chunks")
class ChunkEntity (
    @PrimaryKey @ColumnInfo(name = "id") val chunkId: UUID,
    @ColumnInfo(name = "data") val data: ByteArray,
)
