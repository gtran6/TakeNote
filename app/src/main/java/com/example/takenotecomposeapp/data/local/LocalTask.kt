package com.example.takenotecomposeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Internal model used to represent a task stored locally in a Room database. This is used inside
 * the data layer only.
 *
 * See ModelMapping.kt for mapping functions used to convert this model to other
 * models.
 */
@Entity(tableName = "task")
data class LocalTask(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)
