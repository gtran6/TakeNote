package com.example.takenotecomposeapp.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takenotecomposeapp.R
import com.example.takenotecomposeapp.TakeNoteDestinationsArgs
import com.example.takenotecomposeapp.data.Task
import com.example.takenotecomposeapp.data.TaskRepository
import com.example.takenotecomposeapp.ui.tasks.StopTimeoutMillis
import com.example.takenotecomposeapp.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UiState for the Details screen.
 */
data class TaskDetailUiStates(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskDeleted: Boolean = false
)

/**
 * ViewModel for the Details screen.
 */
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId: String = savedStateHandle[TakeNoteDestinationsArgs.TASK_ID_ARG]!!
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isTaskDeleted = MutableStateFlow(false)
    private val _taskAsync = taskRepository.getTasksStream(taskId)
        .map { handleTask(it) }
        .catch { emit(Async.Error(R.string.loading_tasks_error)) }
    val uiState: StateFlow<TaskDetailUiStates> = combine(
        _userMessage, _isLoading, _isTaskDeleted, _taskAsync
    ) { userMessage, isLoading, isTaskDeleted, taskAsync ->
        when (taskAsync) {
            Async.Loading -> {
                TaskDetailUiStates(isLoading = true)
            }
            is Async.Error -> {
                TaskDetailUiStates(
                    userMessage = taskAsync.errorMessage,
                    isTaskDeleted = isTaskDeleted
                )
            }
            is Async.Success -> {
                TaskDetailUiStates(
                    task = taskAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage,
                    isTaskDeleted = isTaskDeleted
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(StopTimeoutMillis),
        initialValue = TaskDetailUiStates(isLoading = true)
    )

    private fun handleTask(task: Task?): Async<Task?> {
        if (task == null) {
            return Async.Error(R.string.task_not_found)
        }
        return Async.Success(task)
    }

    fun deleteTask() = viewModelScope.launch {
        taskRepository.deleteTask(taskId)
    }

    fun completeTask(isCompleted: Boolean) = viewModelScope.launch {
        val task = uiState.value.task ?: return@launch
        if (isCompleted) {
            taskRepository.completeTask(task.id)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            taskRepository.activateTask(task.id)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }
}