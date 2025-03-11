package com.example.takenotecomposeapp.ui.addedittask

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.takenotecomposeapp.R
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.takenotecomposeapp.util.AddEditTaskTopAppBar

@Composable
fun AddEditTaskScreen(
    modifier: Modifier = Modifier,
    viewModel: AddEditTaskViewModel = hiltViewModel(),
    @StringRes topBarTitle: Int,
    onBackPressed: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onTaskUpdate: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        topBar = {
            AddEditTaskTopAppBar(
                topBarTitle,
                onBackPressed
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = viewModel::saveTask
            ) {
                Icon(Icons.Filled.Done, stringResource(id = R.string.cd_save_task))
            }
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        AddEditTaskContent(
            title = uiState.title,
            description = uiState.description,
            onTitleChanged = viewModel::updateTitle,
            onDescriptionChanged = viewModel::updateDescription,
            modifier = Modifier.padding(paddingValues)
        )

        // Check if the task is saved and call onTaskUpdate event
        LaunchedEffect(uiState.isTaskSaved) {
            if (uiState.isTaskSaved) {
                onTaskUpdate()
            }
        }

        // Check for user messages to display on the screen
        uiState.userMessage?.let { userMessage ->
            val snackbarText = stringResource(userMessage)
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.showSnackbarMessage()
            }
        }

    }
}

@Composable
private fun AddEditTaskContent(
    title: String,
    description: String,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.vertical_margin))
            .verticalScroll(rememberScrollState())
    ) {
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onSecondary
        )
        OutlinedTextField(
            value = title,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onTitleChanged,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.title_hint),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            textStyle = MaterialTheme.typography.headlineSmall
                .copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            colors = textFieldColors
        )
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.description_hint)
                )
            },
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth(),
            colors = textFieldColors
        )
    }
}

@Preview
@Composable
private fun AddEditTaskContentPreview() {
    MaterialTheme {
        Surface {
            AddEditTaskContent(
                title = "",
                description = "",
                {}, {}
            )
        }
    }
}