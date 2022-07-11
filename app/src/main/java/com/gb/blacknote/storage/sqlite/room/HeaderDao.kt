package com.gb.blacknote.storage.sqlite.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import java.util.*

@Dao
interface HeaderDao {
    @Query(value = "SELECT * FROM headers ORDER BY timestamp ASC LIMIT 1")
    fun getHeader(): List<HeaderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceHeader(headerEntity: HeaderEntity)

    @Delete
    fun removeHeader(id: UUID)
}