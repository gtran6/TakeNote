package com.example.takenotecomposeapp.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [TaskRepository]. Single entry point for managing tasks' data.
 *
 * @param networkDataSource - The network data source
 * @param localDataSource - The local data source
 * @param dispatcher - The dispatcher to be used for long running or complex operations, such as ID
 * generation or mapping many models.
 * @param scope - The coroutine scope used for deferred jobs where the result isn't important, such
 * as sending data to the network.
 */
class TaskRepositoryImpl @Inject constructor(

) : TaskRepository {
    override fun getTasksStream(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(taskId: String, description: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }
}