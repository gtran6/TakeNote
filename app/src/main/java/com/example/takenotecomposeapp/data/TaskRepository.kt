package com.example.takenotecomposeapp.data

import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTasksStream(): Flow<List<Task>>

}