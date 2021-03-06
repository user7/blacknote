package com.gb.blacknote.storage.sqlite.room

import androidx.room.*
import java.util.*

@Dao
interface ChunkDao {
    @Query(value = "SELECT * FROM chunks WHERE id = :id")
    fun getChunk(id: UUID): HeaderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceChunk(headerEntity: HeaderEntity)

    @Delete
    fun removeChunk(id: UUID)
}
