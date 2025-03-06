package com.example.takenotecomposeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room Database that contains the Task table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [LocalTask::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}