package com.farmerline.mergedataforms.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmerline.mergedataforms.ui.components.FormRenderer
import com.farmerline.mergedataforms.ui.viewmodel.FormViewModel

@Composable
fun FormListScreen(
    viewModel: FormViewModel = hiltViewModel(),
    onQuestionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    // TODO 07: modify this to use the internet util functions we created in the util package
    val isInternetAvailable by viewModel.isInternetAvailable.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFormConfig("assessment/testjson1.json")
    }

    FormRenderer(
        uiState,
        onAnswerChanged = viewModel::updateAnswer,
        onQuestionSelected = onQuestionSelected,
        onSubmit = viewModel::validateAndSubmit,
        isInternetAvailable = isInternetAvailable
    )
}