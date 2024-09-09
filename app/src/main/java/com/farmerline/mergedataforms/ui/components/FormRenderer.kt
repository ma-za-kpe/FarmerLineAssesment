package com.farmerline.mergedataforms.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farmerline.mergedataforms.domain.model.FormConfig
import com.farmerline.mergedataforms.domain.model.Question
import com.farmerline.mergedataforms.ui.viewmodel.FormViewModel

// TODO: move most of this logic to the forl list screen itself, and leave only reusable components here.

data class FormState(
    val questions: List<Question>,
    val answers: Map<Int, String>,
    val errors: Map<Int, String>
)

@Composable
fun FormRenderer(
    uiState: FormViewModel.FormUiState,
    onAnswerChanged: (Int, String) -> Unit,
    onQuestionSelected: (String) -> Unit,
    onSubmit: () -> Unit,
    isInternetAvailable: Boolean
) {
    when (uiState) {
        is FormViewModel.FormUiState.Loading -> LoadingState()
        is FormViewModel.FormUiState.Success -> SuccessState(
            formConfig = uiState.formConfig,
            onAnswerChanged = onAnswerChanged,
            onQuestionSelected = onQuestionSelected,
            onSubmit = onSubmit,
            isInternetAvailable = isInternetAvailable
        )

        is FormViewModel.FormUiState.Error -> ErrorState(uiState.message)
    }
}

@Composable
fun SuccessState(
    onQuestionSelected: (String) -> Unit,
    onAnswerChanged: (Int, String) -> Unit,
    formConfig: FormConfig,
    onSubmit: () -> Unit,
    isInternetAvailable: Boolean
) {
    // Initialize the formState with default answers from FormConfig, since we are getting it from the romdb
    var formState by remember {
        mutableStateOf(
            FormState(
                questions = formConfig.questions,
                answers = formConfig.questions.associate { it.id to it.defaultAnswer },
                errors = emptyMap()
            )
        )
    }

    // Calculate the progress
    val totalQuestions = formConfig.questions.size
    val filledDefaultAnswers = formState.answers.count { it.value.isNotEmpty() }
    val progressPercentage = (filledDefaultAnswers.toFloat() / totalQuestions.toFloat()) * 100f

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Total Questions: ${formConfig.numberOfQuestions}",
            modifier = Modifier.padding(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = progressPercentage / 100f,
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {}) {
                Text("View Progress")
            }
        }

        LazyColumn {
            items(formConfig.questions) { question ->
                QuestionItem(
                    question = question,
                    answer = formState.answers[question.id] ?: "",
                    error = formState.errors[question.id],
                    onQuestionSelected = onQuestionSelected,
                    onAnswerChanged = { newAnswer: String ->
                        formState = formState.copy(
                            answers = formState.answers + (question.id to newAnswer),
                            errors = formState.errors - question.id
                        )
                        onAnswerChanged(question.id, newAnswer)
                    }
                )
            }

            item {
                // Submit button
                Button(
                    onClick = {
                        val newErrors = formConfig.questions.associate { question ->
                            question.id to (if (formState.answers[question.id].isNullOrBlank()) "This field is required" else "")
                        }.filterValues { it.isNotBlank() }

                        if (newErrors.isEmpty()) {
                            Log.d("FormRenderer", "No errors")
                            onSubmit()
                        } else {
                            Log.d("FormRenderer", "Errors: $newErrors")
                            formState = formState.copy(errors = newErrors)
                        }
                    },
                    enabled = (isInternetAvailable || progressPercentage == 1f) && formState.answers.isNotEmpty()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: Question,
    answer: String,
    error: String?,
    onQuestionSelected: (String) -> Unit,
    onAnswerChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable { onQuestionSelected(question.id.toString()) }
            .padding(16.dp)
    ) {
        Text(
            text = "Question ${question.questionNumber}: ${question.title}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        when (question) {
            is Question.EditText -> EditTextQuestion(question, answer, error, onAnswerChanged)
            is Question.RadioButtons -> RadioButtonsQuestion(
                question,
                answer,
                error,
                onAnswerChanged
            )
        }
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun EditTextQuestion(
    question: Question.EditText,
    answer: String,
    error: String?,
    onAnswerChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = answer,
        onValueChange = { onAnswerChanged(it) },
        label = { Text("Enter your answer") },
        modifier = Modifier.fillMaxWidth(),
        isError = error != null
    )
}

@Composable
fun RadioButtonsQuestion(
    question: Question.RadioButtons,
    answer: String,
    error: String?,
    onAnswerChanged: (String) -> Unit
) {
    Column {
        question.options.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option.name == answer),
                        onClick = { onAnswerChanged(option.name) }
                    )
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = (option.name == answer),
                    onClick = null
                )
                Text(
                    text = option.name,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = androidx.compose.ui.graphics.Color.Red)
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
