package com.example.takenotecomposeapp.ui.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takenotecomposeapp.ADD_EDIT_RESULT_OK
import com.example.takenotecomposeapp.DELETE_RESULT_OK
import com.example.takenotecomposeapp.EDIT_RESULT_OK
import com.example.takenotecomposeapp.Hilt_TakeNoteActivity
import com.example.takenotecomposeapp.R
import com.example.takenotecomposeapp.TakeNoteDestinationsArgs
import com.example.takenotecomposeapp.data.Task
import com.example.takenotecomposeapp.data.TaskRepository
import com.example.takenotecomposeapp.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UiState for the task list screen.
 */
data class TasksUiState(
    val items: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val filteringInfo: FilteringUiInfo = FilteringUiInfo(),
    val userMessage: Int? = null,
    val isTaskDeleted: Boolean = false
)

data class FilteringUiInfo(
    val currentFilteringLabel: Int = R.string.all_tasks,
    val noTasksLabel: Int = R.string.no_tasks,
    val noTaskIcon: Int = R.drawable.twotone_checklist_24
)

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _savedFilterType = savedStateHandle.getStateFlow(TASKS_FILTER_SAVED_STATE_KEY,
        TasksFilterType.ALL_TASKS
    )
    private val _filterUiInfo = _savedFilterType.map { getFilterUiInfo(it) }.distinctUntilChanged()
    private val _isLoading = MutableStateFlow(false)
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isTaskDeleted = MutableStateFlow(false)
    private val _filterTasksAsync = combine(taskRepository.getTasksStream(), _savedFilterType) { tasks, type ->
        filterTasks(tasks, type)
    }
        .map { Async.Success(it) }
        .catch<Async<List<Task>>> { emit(Async.Error(R.string.loading_tasks_error)) }

    val uiState: StateFlow<TasksUiState> = combine(
        _filterUiInfo, _isLoading, _userMessage, _filterTasksAsync, _isTaskDeleted
    ) { filterUiInfo, isLoading, userMessage, filterTasksAsync, isTaskDeleted ->
        when (filterTasksAsync) {
            Async.Loading -> {
                TasksUiState(isLoading = true)
            }
            is Async.Error -> {
                TasksUiState(
                    userMessage = filterTasksAsync.errorMessage,
                    isTaskDeleted = isTaskDeleted)
            }
            is Async.Success -> {
                TasksUiState(
                    items = filterTasksAsync.data,
                    filteringInfo = filterUiInfo,
                    isLoading = isLoading,
                    userMessage = userMessage,
                    isTaskDeleted = isTaskDeleted
                )
            }
        }

    }.stateIn(
        scope = viewModelScope,
        /**
         * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
         *
         * When the UI stops observing, upstream flows stay active for some time to allow the system to
         * come back from a short-lived configuration change (such as rotations). If the UI stops
         * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
         * back, the latest value is replayed and the upstream flows are executed again. This is done to
         * save resources when the app is in the background but let users switch between apps quickly.
         */
        started = SharingStarted.WhileSubscribed(StopTimeoutMillis),
        initialValue = TasksUiState(isLoading = true)
    )


    private fun getFilterUiInfo(requestType: TasksFilterType): FilteringUiInfo =
        when (requestType) {
            TasksFilterType.ALL_TASKS -> {
                FilteringUiInfo(
                    R.string.all_tasks, R.string.no_tasks, R.drawable.twotone_checklist_24
                )
            }
            TasksFilterType.ACTIVE_TASKS -> {
                FilteringUiInfo(
                    R.string.label_active, R.string.no_tasks_active, R.drawable.ic_check_circle_96dp
                )
            }
            TasksFilterType.COMPLETED_TASKS -> {
                FilteringUiInfo(
                    R.string.label_completed, R.string.no_tasks_completed, R.drawable.ic_verified_user_96dp
                )
            }
        }

    private fun filterTasks(tasks: List<Task>, filteringType: TasksFilterType): List<Task> {
        val tasksToShow = ArrayList<Task>()
        for (task in tasks) {
            when (filteringType) {
                TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                TasksFilterType.ACTIVE_TASKS -> if (task.isActive) {
                    tasksToShow.add(task)
                }
                TasksFilterType.COMPLETED_TASKS -> if (task.isCompleted) {
                    tasksToShow.add(task)
                }
            }
        }
        return tasksToShow
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if (completed) {
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

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_task_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_task_message)
            DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_task_message)
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun setFiltering(requestType: TasksFilterType) {
        savedStateHandle[TASKS_FILTER_SAVED_STATE_KEY] = requestType
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskRepository.deleteTask(task.id)
    }

    private fun handleTask(task: Task?): Async<Task?> {
        if (task == null) {
            return Async.Error(R.string.task_not_found)
        }
        return Async.Success(task)
    }
}

// Used to save the current filtering in SavedStateHandle.
const val TASKS_FILTER_SAVED_STATE_KEY = "TASKS_FILTER_SAVED_STATE_KEY"
const val StopTimeoutMillis: Long = 5000