package com.gb.blacknote.db.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        HeaderEntity::class,
        ChunkEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun headerDao(): HeaderDao
    abstract fun chunkDao(): ChunkDao
}