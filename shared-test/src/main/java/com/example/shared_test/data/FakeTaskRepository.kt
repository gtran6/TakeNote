package com.example.shared_test.data

import com.example.takenotecomposeapp.data.Task
import com.example.takenotecomposeapp.data.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeTaskRepository: TaskRepository {
    private val _savedTasks = MutableStateFlow(LinkedHashMap<String, Task>())
    val savedTasks: StateFlow<LinkedHashMap<String, Task>> = _savedTasks.asStateFlow()

    private var shouldThrowError = false

    private val observableTasks: Flow<List<Task>> = savedTasks.map {
        if (shouldThrowError) {
            throw Exception("")
        } else {
            it.values.toList()
        }
    }
    override fun getTasksStream(): Flow<List<Task>> = observableTasks

    override fun getTasksStream(taskId: String): Flow<Task?> {
        return observableTasks.map { tasks ->
            return@map tasks.firstOrNull { it.id == taskId}
        }
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(title: String, description: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskId: String, title: String, description: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }
}