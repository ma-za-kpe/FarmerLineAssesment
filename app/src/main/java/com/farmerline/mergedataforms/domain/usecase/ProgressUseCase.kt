package com.farmerline.mergedataforms.domain.usecase

import android.util.Log
import com.farmerline.mergedataforms.data.repository.FormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProgressUseCase @Inject constructor(
    private val formRepository: FormRepository
) {
    suspend fun updateAnswer(endpoint: String, questionId: Int, newDefaultAnswer: String) {
        Log.d("TAG", "updateAnswer: running")
        formRepository.updateDefaultAnswer(endpoint, questionId, newDefaultAnswer)
    }
}