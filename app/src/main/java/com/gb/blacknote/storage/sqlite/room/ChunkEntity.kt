package com.gb.blacknote.storage.sqlite.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chunks")
class ChunkEntity (
    @PrimaryKey @ColumnInfo(name = "id") val id: UUID,
    @ColumnInfo(name = "data") val data: ByteArray,
)
