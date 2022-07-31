package com.gb.blacknote.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "headers")
class HeaderEntity (
    @PrimaryKey @ColumnInfo(name = "id") val headerId: UUID,
    @ColumnInfo(name = "data") val data: ByteArray,
    @ColumnInfo(name = "epochMs") val epochMs: Long,
)
