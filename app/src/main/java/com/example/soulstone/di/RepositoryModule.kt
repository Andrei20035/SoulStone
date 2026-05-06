package com.example.soulstone.di

import com.example.soulstone.data.repository.BenefitRepository
import com.example.soulstone.data.repository.BenefitRepositoryImpl
import com.example.soulstone.data.repository.ChakraRepository
import com.example.soulstone.data.repository.ChakraRepositoryImpl
import com.example.soulstone.data.repository.ChineseZodiacSignRepository
import com.example.soulstone.data.repository.ChineseZodiacSignRepositoryImpl
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
    abstract fun bindBenefitRepository(
        benefitRepositoryImpl: BenefitRepositoryImpl
    ): BenefitRepository

    @Binds
    @Singleton
    abstract fun bindChakraRepository(
        chakraRepositoryImpl: ChakraRepositoryImpl
    ): ChakraRepository

    @Binds
    @Singleton
    abstract fun bindChineseZodiacSignRepository(
        chineseZodiacSignRepositoryImpl: ChineseZodiacSignRepositoryImpl
    ): ChineseZodiacSignRepository

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
    abstract fun bindZodiacSignRepository(
        zodiacSignRepositoryImpl: ZodiacSignRepositoryImpl
    ): ZodiacSignRepository
}
