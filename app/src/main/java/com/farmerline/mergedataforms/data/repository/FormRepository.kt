package com.farmerline.mergedataforms.data.repository

import com.farmerline.mergedataforms.data.models.FormConfigResponse

interface FormRepository {
    suspend fun getFormConfig(endpoint: String): FormConfigResponse
    suspend fun getLocalFormConfig(endpoint: String): FormConfigResponse?
    suspend fun updateDefaultAnswer(endpoint: String, questionId: Int, newDefaultAnswer: String)
}