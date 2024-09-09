package com.farmerline.mergedataforms.domain.model

sealed class Question {
    abstract val id: Int
    abstract val title: String
    abstract val questionNumber: Int
    abstract val defaultAnswer: String

    data class EditText(
        override val id: Int,
        override val title: String,
        override val questionNumber: Int,
        override val defaultAnswer: String
    ) : Question()

    data class RadioButtons(
        override val id: Int,
        override val title: String,
        override val questionNumber: Int,
        override val defaultAnswer: String,
        val options: List<RadioButtonOption>
    ) : Question()
}

data class RadioButtonOption(
    val id: Int,
    val name: String
)