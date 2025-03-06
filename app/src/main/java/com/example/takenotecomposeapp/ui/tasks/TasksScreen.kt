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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.takenotecomposeapp.MainTheme
import com.example.takenotecomposeapp.R
import com.example.takenotecomposeapp.data.Task

fun TasksScreen() {

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
    modifier: Modifier = Modifier
) {
    Column(

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

            }
        }
    }
}

@Preview
@Composable
private fun TasksContentPreview() {
    MaterialTheme {
        Surface {

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