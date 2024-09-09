package com.farmerline.mergedataforms.data.cache

import android.util.Log
import com.farmerline.mergedataforms.data.cache.daos.FormConfigDao
import com.farmerline.mergedataforms.data.cache.entity.FormConfigEntity
import com.farmerline.mergedataforms.data.models.FormConfigResponse
import javax.inject.Inject

class LocalCacheImpl @Inject constructor(
    private val formConfigDao: FormConfigDao
) : LocalCache {
    override suspend fun saveFormConfig(
        endpoint: String,
        formConfig: FormConfigResponse,
        progress: Float
    ) {
        val entity = FormConfigEntity(
            endpoint = endpoint,
            questions = formConfig.questions,
            numberOfQuestions = formConfig.number_of_questions,
            progress = progress,
        )
        formConfigDao.insertFormConfig(entity)
    }

    override suspend fun getFormConfig(endpoint: String): FormConfigResponse? {
        val entity = formConfigDao.getFormConfig(endpoint)
        return entity?.let {
            FormConfigResponse(
                questions = it.questions,
                number_of_questions = it.numberOfQuestions
            )
        }
    }

    override suspend fun updateDefaultAnswer(
        endpoint: String,
        questionId: Int,
        newDefaultAnswer: String
    ) {
        Log.d("TAG", "updateAnswer: running 3")
        formConfigDao.updateDefaultAnswer(endpoint, questionId, newDefaultAnswer)
    }
}