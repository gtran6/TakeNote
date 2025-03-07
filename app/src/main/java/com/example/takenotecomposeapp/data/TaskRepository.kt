package com.example.takenotecomposeapp.data

import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTasksStream(): Flow<List<Task>>

    suspend fun completeTask(taskId: String)

    suspend fun createTask(title: String, description: String): String

    suspend fun activateTask(taskId: String)

    fun getTasksStream(taskId: String): Flow<Task?>

    suspend fun deleteTask(taskId: String)
}