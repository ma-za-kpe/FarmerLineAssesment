package com.farmerline.mergedataforms.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.farmerline.mergedataforms.data.cache.QuestionListConverter
import com.farmerline.mergedataforms.data.models.QuestionDto

@Entity(tableName = "form_configs")
data class FormConfigEntity(
    @PrimaryKey val endpoint: String,
    @TypeConverters(QuestionListConverter::class)
    val questions: List<QuestionDto>,
    val numberOfQuestions: Int,
    val progress: Float,
)