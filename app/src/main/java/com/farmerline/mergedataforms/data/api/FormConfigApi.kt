package com.farmerline.mergedataforms.data.api

import com.farmerline.mergedataforms.data.models.FormConfigResponse
import retrofit2.http.GET

interface FormConfigApi {
    @GET("assessment/testjson1.json")
    suspend fun getFormConfig1(): FormConfigResponse

    @GET("assessment/testjson2.json")
    suspend fun getFormConfig2(): FormConfigResponse
}