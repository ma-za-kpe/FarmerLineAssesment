package com.farmerline.mergedataforms.data.di

import com.farmerline.mergedataforms.data.api.FormConfigApi
import com.farmerline.mergedataforms.data.cache.LocalCache
import com.farmerline.mergedataforms.data.repository.FormRepository
import com.farmerline.mergedataforms.data.repository.FormRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideFormRepository(
        apiService: FormConfigApi,
        localCache: LocalCache
    ): FormRepository {
        return FormRepositoryImpl(apiService, localCache)
    }
}