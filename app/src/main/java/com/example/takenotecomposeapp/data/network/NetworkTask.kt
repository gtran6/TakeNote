package com.example.takenotecomposeapp.data.network

/**
 * Internal model used to represent a task obtained from the network. This is used inside the data
 * layer only.
 *
 * See ModelMapping.kt for mapping functions used to convert this model to other models.
 */
data class NetworkTask(
    val id: String,
    val title: String,
    val shortDescription: String,
    val priority: Int? = null,
    val status: TaskStatus = TaskStatus.ACTIVE
)

enum class TaskStatus {
    ACTIVE,
    COMPLETE
}
