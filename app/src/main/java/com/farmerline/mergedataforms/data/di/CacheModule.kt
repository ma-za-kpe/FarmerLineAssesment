package com.farmerline.mergedataforms.data.di

import android.content.Context
import androidx.room.Room
import com.farmerline.mergedataforms.data.cache.LocalCache
import com.farmerline.mergedataforms.data.cache.LocalCacheImpl
import com.farmerline.mergedataforms.data.cache.MergeDatatabase
import com.farmerline.mergedataforms.data.cache.daos.FormConfigDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: LocalCacheImpl): LocalCache

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): MergeDatatabase {
            return Room.databaseBuilder(
                context,
                MergeDatatabase::class.java,
                "merge.db"
            )
                .build()
        }

        @Provides
        fun provideFormConfigDao(
            mergeDatatabase: MergeDatatabase
        ): FormConfigDao = mergeDatatabase.formConfigDao()
    }
}