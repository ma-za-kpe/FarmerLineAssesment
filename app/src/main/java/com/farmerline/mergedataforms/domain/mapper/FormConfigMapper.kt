package com.farmerline.mergedataforms.domain.mapper

import com.farmerline.mergedataforms.data.models.FormConfigResponse
import com.farmerline.mergedataforms.data.models.QuestionDto
import com.farmerline.mergedataforms.data.models.RadioButtonOptionDto
import com.farmerline.mergedataforms.domain.model.FormConfig
import com.farmerline.mergedataforms.domain.model.Question
import com.farmerline.mergedataforms.domain.model.RadioButtonOption

object FormConfigMapper {
    fun mapToDomain(response: FormConfigResponse): FormConfig {
        return FormConfig(
            questions = response.questions.map { it.toDomainQuestion() },
            numberOfQuestions = response.number_of_questions
        )
    }

    private fun QuestionDto.toDomainQuestion(): Question {
        return when (form_type) {
            "EDIT_TEXT" -> Question.EditText(
                id = id,
                title = title,
                questionNumber = question_number,
                defaultAnswer = defaultAnswer
            )
            "RADIO_BUTTONS" -> Question.RadioButtons(
                id = id,
                title = title,
                questionNumber = question_number,
                defaultAnswer = defaultAnswer,
                options = radio_button_option.map { it.toDomainRadioButtonOption() }
            )
            else -> throw IllegalArgumentException("Unknown form type: $form_type")
        }
    }

    private fun RadioButtonOptionDto.toDomainRadioButtonOption(): RadioButtonOption {
        return RadioButtonOption(
            id = id,
            name = name
        )
    }
}