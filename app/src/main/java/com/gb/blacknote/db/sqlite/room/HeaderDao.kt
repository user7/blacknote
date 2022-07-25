package com.gb.blacknote.db.sqlite.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import java.util.*

@Dao
interface HeaderDao {
    @Query(value = "SELECT * FROM headers ORDER BY timestamp DESC LIMIT 1")
    fun getLatestHeader(): HeaderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceHeader(headerEntity: HeaderEntity)

    @Delete
    fun removeHeader(id: UUID)
}