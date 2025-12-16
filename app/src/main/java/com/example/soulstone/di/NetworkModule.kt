package com.example.soulstone.di

import com.example.soulstone.data.remote.TranslationApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTranslationApi(): TranslationApiService {
        return Retrofit.Builder()
            .baseUrl("https://translation.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationApiService::class.java)
    }
}