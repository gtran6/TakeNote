package com.example.takenotecomposeapp.ui.tasks

import androidx.compose.material3.Checkbox
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.takenotecomposeapp.MainTheme
import com.example.takenotecomposeapp.R
import com.example.takenotecomposeapp.data.Task
import com.example.takenotecomposeapp.util.TakeNoteTopAppBars

@Composable
fun TasksScreen(
    @StringRes userMessage: Int,
    modifier: Modifier = Modifier,
    onAddTask: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel(),
    onTaskClick: (Task) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    openDrawer: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            SmallFloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_task))
            }
        },
        topBar = {
            TakeNoteTopAppBars(
                openDrawer = openDrawer,
                onFilterAllTasks = { viewModel.setFiltering(TasksFilterType.ALL_TASKS) },
                onFilterActiveTasks = { viewModel.setFiltering(TasksFilterType.ACTIVE_TASKS) },
                onFilterCompletedTasks = { viewModel.setFiltering(TasksFilterType.COMPLETED_TASKS) },
            )
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        TasksContent(
            tasks = uiState.items,
            currentFilteringLabel = uiState.filteringInfo.currentFilteringLabel,
            onTaskClick = onTaskClick,
            onTaskCheckedChange = viewModel::completeTask,
            modifier = Modifier.padding(paddingValues)
        )

        // Check for user messages to display on the screen
        uiState.userMessage?.let { message ->
            val snackBarText = stringResource(message)
            LaunchedEffect(snackbarHostState, viewModel, message, snackBarText) {
                snackbarHostState.showSnackbar(snackBarText)
                viewModel.snackbarMessageShown()
            }
        }

        // Check if there's a userMessage to show to the user
        val currentOnUserMessageDisplayed by rememberUpdatedState(onUserMessageDisplayed)
        LaunchedEffect(userMessage) {
            if (userMessage != 0) {
                viewModel.showEditResultMessage(userMessage)
                currentOnUserMessageDisplayed()
            }
        }
    }
}

@Composable
private fun TasksEmptyContent(
    @StringRes noTasksLabel: Int,
    @DrawableRes noTasksIcon: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = noTasksIcon),
            contentDescription = stringResource(R.string.no_tasks_image_content_description),
            modifier = Modifier.size(98.dp)
        )
    }
}

@Preview
@Composable
private fun TasksEmptyContentPreview() {
    MainTheme {
        Surface {
            TasksEmptyContent(
                noTasksLabel = R.string.no_tasks,
                noTasksIcon = R.drawable.twotone_checklist_24
            )
        }
    }
}

@Composable
private fun TasksContent(
    tasks: List<Task>,
    @StringRes currentFilteringLabel: Int,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.vertical_margin))
    ) {
        Text(
            text = stringResource(currentFilteringLabel),
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding),
                vertical = dimensionResource(id = R.dimen.vertical_margin)
            ),
            style = MaterialTheme.typography.headlineMedium
        )
        LazyColumn {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onTaskClick = onTaskClick,
                    onCheckedChange = { onTaskCheckedChange(task, it) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun TasksContentPreview() {
    MaterialTheme {
        Surface {
            TasksContent(
                tasks = listOf(
                    Task(
                        title = "Title 1",
                        description = "Description 1",
                        isCompleted = false,
                        id = "ID 1"
                    ),
                    Task(
                        title = "Title 2",
                        description = "Description 2",
                        isCompleted = true,
                        id = "ID 2"
                    ),
                    Task(
                        title = "Title 3",
                        description = "Description 3",
                        isCompleted = true,
                        id = "ID 3"
                    ),
                    Task(
                        title = "Title 4",
                        description = "Description 4",
                        isCompleted = false,
                        id = "ID 4"
                    )
                ),
                currentFilteringLabel = R.string.all_tasks,
                onTaskClick = {},
                onTaskCheckedChange = { _, _ -> }
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onTaskClick: (Task) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.vertical_margin),
                vertical = dimensionResource(id = R.dimen.list_item_padding)
            ).clickable { onTaskClick(task) }
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = task.titleForList,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.vertical_margin)
            ),
            textDecoration = if (task.isCompleted) {
                TextDecoration.LineThrough
            } else {
                null
            }
        )
    }
}

@Preview
@Composable
private fun TaskItemPreview() {
    MaterialTheme {
        Surface {
            TaskItem(
                task = Task(
                    title = "Title",
                    description = "Description",
                    id = "ID"
                ),
                onTaskClick = {},
                onCheckedChange = {}
            )
        }
    }
}

@Preview
@Composable
private fun TaskItemCompletedPreview() {
    MaterialTheme {
        Surface {
            TaskItem(
                task = Task(
                    title = "Title",
                    description = "Description",
                    isCompleted = true,
                    id = "ID"
                ),
                onTaskClick = {},
                onCheckedChange = {}
            )
        }
    }
}