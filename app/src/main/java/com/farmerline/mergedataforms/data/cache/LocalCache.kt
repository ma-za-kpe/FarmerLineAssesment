package com.farmerline.mergedataforms.data.cache

import com.farmerline.mergedataforms.data.models.FormConfigResponse

interface LocalCache {
    suspend fun saveFormConfig(endpoint: String, formConfig: FormConfigResponse, progress: Float)
    suspend fun getFormConfig(endpoint: String): FormConfigResponse?
    suspend fun updateDefaultAnswer(endpoint: String, questionId: Int, newDefaultAnswer: String)
}
