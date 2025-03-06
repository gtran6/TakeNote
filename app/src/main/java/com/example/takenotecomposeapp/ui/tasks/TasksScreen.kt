package com.example.takenotecomposeapp.ui.tasks

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.takenotecomposeapp.MainTheme
import com.example.takenotecomposeapp.R

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