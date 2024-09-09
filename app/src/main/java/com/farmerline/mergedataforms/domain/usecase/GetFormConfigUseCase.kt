package com.farmerline.mergedataforms.domain.usecase

import com.farmerline.mergedataforms.data.repository.FormRepository
import com.farmerline.mergedataforms.domain.mapper.FormConfigMapper
import com.farmerline.mergedataforms.domain.model.FormConfig
import timber.log.Timber
import javax.inject.Inject

class GetFormConfigUseCase @Inject constructor(
    private val repository: FormRepository
) {
    suspend operator fun invoke(endpoint: String): FormConfig {
        val response = repository.getFormConfig(endpoint)
        Timber.d("response: $response")
        return FormConfigMapper.mapToDomain(response)
    }
}

class GetLocalFormConfigUseCase @Inject constructor(
    private val repository: FormRepository
) {
    suspend operator fun invoke(endpoint: String): FormConfig? {
        val response = repository.getLocalFormConfig(endpoint)
        Timber.d("updateAnswer response local: $response")
        return response?.let { FormConfigMapper.mapToDomain(it) }
    }
}