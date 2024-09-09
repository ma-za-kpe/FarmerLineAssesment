package com.farmerline.mergedataforms.data.repository

import android.util.Log
import com.farmerline.mergedataforms.data.api.FormConfigApi
import com.farmerline.mergedataforms.data.cache.LocalCache
import com.farmerline.mergedataforms.data.models.FormConfigResponse
import javax.inject.Inject


class FormRepositoryImpl @Inject constructor(
    private val api: FormConfigApi,
    private val localCache: LocalCache
) : FormRepository {
    override suspend fun getFormConfig(endpoint: String): FormConfigResponse {
        try {
            // Try to get data from cache first
            localCache.getFormConfig(endpoint)?.let { cachedConfig ->
                // Check if the cached config is not empty
                if (cachedConfig.questions.isNotEmpty()) {
                    Log.d("FormConfigRepository", "updateAnswer: Returning config from local cache for endpoint: $endpoint")
                    return cachedConfig
                }
            }

            // If not in cache or cache is empty, fetch from network
            // TODO: 10, i dont understand where to use these two endpoints, they are my only roadblaock
            Log.d("FormConfigRepository", "updateAnswer: Fetching config from network for endpoint: $endpoint")
            val networkResponse = when (endpoint) {
                "assessment/testjson1.json" -> api.getFormConfig1()
                "assessment/testjson2.json" -> api.getFormConfig2()
                else -> throw IllegalArgumentException("Invalid endpoint: $endpoint")
            }

            // Cache the new data
            localCache.saveFormConfig(endpoint, networkResponse, 0f)
            Log.d("FormConfigRepository", "updateAnswer: Saved network response to local cache for endpoint: $endpoint")

            return localCache.getFormConfig("assessment/testjson1.json") ?: throw IllegalStateException("Failed to retrieve cached config after network fetch")

        } catch (e: Exception) {
            Log.e("FormConfigRepository", "updateAnswer: Error fetching form config for endpoint: $endpoint", e)
            throw e
        }
    }

    override suspend fun getLocalFormConfig(endpoint: String): FormConfigResponse? {
        return localCache.getFormConfig(endpoint)
    }

    override suspend fun updateDefaultAnswer(
        endpoint: String,
        questionId: Int,
        newDefaultAnswer: String
    ) {
        Log.d("TAG", "updateAnswer: running 2")
        localCache.updateDefaultAnswer(endpoint, questionId, newDefaultAnswer)
    }

    private fun calculateProgress(formConfig: FormConfigResponse, answers: Map<Int, String>): Float {
        val answeredQuestions = answers.count { it.value.isNotBlank() }
        return answeredQuestions.toFloat() / formConfig.number_of_questions
    }
}
