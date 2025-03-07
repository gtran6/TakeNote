package com.example.takenotecomposeapp.ui.taskdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.takenotecomposeapp.R
import com.example.takenotecomposeapp.data.Task

@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        floatingActionButton = {
            SmallFloatingActionButton(onClick = {}) {
                Icon(Icons.Filled.Edit, stringResource(id = R.string.edit_task))
            }
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        EditTaskContent(
            task = uiState.task,
            onTaskCheck = viewModel::completeTask,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun EditTaskContent(
    task: Task?,
    onTaskCheck: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val padding = Modifier.padding(
        horizontal = dimensionResource(id = R.dimen.vertical_margin),
        vertical = dimensionResource(id = R.dimen.list_item_padding)
    )
    val commonModifier = modifier
        .fillMaxWidth()
        .then(padding)
    Column(commonModifier.verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (task != null) {
                Checkbox(task.isCompleted, onTaskCheck)
                Column {
                    Text(text = task.title, style = MaterialTheme.typography.headlineSmall)
                    Text(text = task.description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditTaskContentPreview() {
    Surface {
        EditTaskContent(
            task = Task(
                title = "Title",
                description = "Description",
                isCompleted = false,
                id = "ID"
            ),
            onTaskCheck = {}
        )
    }
}

@Preview
@Composable
private fun EditTaskContentCompletedPreview() {
    Surface {
        EditTaskContent(
            task = Task(
                title = "Title",
                description = "Description",
                isCompleted = true,
                id = "ID"
            ),
            onTaskCheck = {}
        )
    }
}
