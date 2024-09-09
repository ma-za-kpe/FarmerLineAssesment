package com.farmerline.mergedataforms.data.cache.daos

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.farmerline.mergedataforms.data.cache.entity.FormConfigEntity
import com.farmerline.mergedataforms.data.models.QuestionDto

@Dao
interface FormConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormConfig(formConfig: FormConfigEntity)

    @Query("SELECT * FROM form_configs WHERE endpoint = :endpoint")
    suspend fun getFormConfig(endpoint: String): FormConfigEntity?

    @Query("UPDATE form_configs SET questions = :updatedQuestions WHERE endpoint = :endpoint")
    fun updateQuestions(endpoint: String, updatedQuestions: List<QuestionDto>)

    @Update
    suspend fun updateFormConfig(formConfig: FormConfigEntity)

    @Transaction
    suspend fun updateDefaultAnswer(endpoint: String, questionId: Int, newDefaultAnswer: String) {
        Log.d("TAG", "updateAnswer: running 4")
        Log.d("updateAnswer: ", "Updating default answer for endpoint: $endpoint, questionId: $questionId, newDefaultAnswer: $newDefaultAnswer")

        val formConfig = getFormConfig(endpoint)
        formConfig?.let {
            val updatedQuestions = it.questions.map { question ->
                if (question.id == questionId) {
                    Log.d("updateAnswer: ", "Updating question: ${question.id}")
                    question.copy(defaultAnswer = newDefaultAnswer)
                } else {
                    question
                }
            }

            val updatedFormConfig = it.copy(questions = updatedQuestions)
            updateFormConfig(updatedFormConfig)

            Log.d("updateAnswer: ","Updated FormConfig: $updatedFormConfig")
        } ?: run {
            Log.d("updateAnswer: ","FormConfig not found for endpoint: $endpoint")
        }
    }
}