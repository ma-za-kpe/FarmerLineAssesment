package com.farmerline.mergedataforms.data.models

data class FormConfigResponse(
    val questions: List<QuestionDto>,
    val number_of_questions: Int
)
