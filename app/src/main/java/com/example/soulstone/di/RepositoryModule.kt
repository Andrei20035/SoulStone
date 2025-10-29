package com.example.soulstone.di

import com.example.soulstone.data.repository.SettingsRepository
import com.example.soulstone.data.repository.SettingsRepositoryImpl
import com.example.soulstone.data.repository.StoneRepository
import com.example.soulstone.data.repository.StoneRepositoryImpl
import com.example.soulstone.data.repository.ZodiacSignRepository
import com.example.soulstone.data.repository.ZodiacSignRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that tells Hilt how to provide implementations for repository interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds [SettingsRepositoryImpl] to the [SettingsRepository] interface.
     * Hilt will provide an instance of SettingsRepositoryImpl.kt whenever
     * a SettingsRepository is requested.
     */
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindStoneRepository(
        stoneRepositoryImpl: StoneRepositoryImpl
    ): StoneRepository

    @Binds
    @Singleton
    abstract fun bindZodiacRepository(
        zodiacRepositoryImpl: ZodiacSignRepositoryImpl
    ): ZodiacSignRepository
}
