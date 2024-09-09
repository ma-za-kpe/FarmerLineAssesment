package com.farmerline.mergedataforms.data.models

data class QuestionDto(
    val id: Int,
    val title: String,
    val question_number: Int,
    val form_type: String,
    val radio_button_option: List<RadioButtonOptionDto>,
    val defaultAnswer: String
)
