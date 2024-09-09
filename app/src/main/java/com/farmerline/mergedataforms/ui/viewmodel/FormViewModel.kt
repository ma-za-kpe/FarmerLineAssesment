package com.farmerline.mergedataforms.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmerline.mergedataforms.domain.model.FormConfig
import com.farmerline.mergedataforms.domain.usecase.GetFormConfigUseCase
import com.farmerline.mergedataforms.domain.usecase.ProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val getFormConfigUseCase: GetFormConfigUseCase,
    private val progressUseCase: ProgressUseCase,
) : ViewModel() {

    sealed class FormUiState {
        object Loading : FormUiState()

        data class Success(
            val formConfig: FormConfig,
            val answers: Map<Int, String>,  // Fetch answers from FormConfig such that we can show/ update them to the user in realtime
            val errors: Map<Int, String>
        ) : FormUiState() {
            companion object {
                fun fromFormConfig(config: FormConfig): Success {
                    val answers = config.questions.associate { it.id to it.defaultAnswer }
                    return Success(formConfig = config, answers = answers, errors = emptyMap())
                }
            }
        }

        data class Error(val message: String) : FormUiState()
    }


    private val _uiState = MutableStateFlow<FormUiState>(FormUiState.Loading)
    val uiState: StateFlow<FormUiState> = _uiState

    private val _isInternetAvailable = MutableStateFlow(false)
    val isInternetAvailable: StateFlow<Boolean> = _isInternetAvailable.asStateFlow()

    private var currentEndpoint: String? = null

    fun loadFormConfig(endpoint: String) {
        viewModelScope.launch {
            _uiState.value = FormUiState.Loading
            try {
                val config = getFormConfigUseCase(endpoint)
                Log.d("TAG", "updateAnswer loadFormConfig: $config")
                _uiState.value = FormUiState.Success.fromFormConfig(config)
            } catch (e: Exception) {
                _uiState.value = FormUiState.Error("Failed to load form configuration: ${e}")
            }
        }
    }

    fun updateAnswer(questionId: Int, answer: String) {
        Log.d("updateAnswer", "questionId: $questionId, answer: $answer")
        // here, we are updating state
        (_uiState.value as? FormUiState.Success)?.let { currentState ->
            _uiState.value = currentState.copy(
                errors = currentState.errors - questionId
            )

            // Update the default answer locally
            viewModelScope.launch {
                try {
                    // Call the local cache to update the default answer
                    Log.d("TAG", "updateAnswer: ${currentEndpoint.toString()}... assessment/testjson1.json ... ${questionId} ... ${answer}")
                    progressUseCase.updateAnswer("assessment/testjson1.json", questionId, answer)

                    // Fetch the updated form config
                    val updatedFormConfig = getFormConfigUseCase(currentEndpoint.toString())

                    // Update the UI state with the new form config
                    updatedFormConfig?.let { config ->
                        _uiState.value = FormUiState.Success(
                            formConfig = FormConfig(
                                questions = config.questions,
                                numberOfQuestions = config.numberOfQuestions,
                            ),
                            answers = currentState.answers,
                            errors = currentState.errors
                        )
                    }
                    Log.d("TAG", "updateAnswer: $updatedFormConfig")
                } catch (e: Exception) {
                    Timber.e(e, "updateAnswer Failed to update answer $e")
                }
            }
        }
        Log.d("updateAnswer", "Updated form config: ${(_uiState.value as? FormUiState.Success)?.formConfig}")
    }

    fun validateAndSubmit() {
        (_uiState.value as? FormUiState.Success)?.let { currentState ->
            val newErrors = currentState.formConfig.questions.associate { question ->
                question.id to (if (currentState.answers[question.id].isNullOrBlank()) "This field is required" else "")
            }.filterValues { it.isNotEmpty() }

            if (newErrors.isEmpty()) {
                Log.d("FormRenderer", "answers ${currentState.answers}")
                submitForm(currentState.answers)
            } else {
                _uiState.value = currentState.copy(errors = newErrors)
            }
        }
    }

    private fun submitForm(answers: Map<Int, String>) {
        Timber.d("Submitting form with answers: $answers")
        // TODO 03: implement a post request to the merge data API
    }
}