package com.example.soulstone.di

import android.content.Context
import androidx.room.Room
import com.example.soulstone.data.dao.BenefitDao
import com.example.soulstone.data.dao.ChakraDao
import com.example.soulstone.data.dao.ChineseZodiacSignDao
import com.example.soulstone.data.dao.StoneDao
import com.example.soulstone.data.dao.ZodiacSignDao
import com.example.soulstone.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of the [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        // 1. Hilt will inject the callback we're about to provide
        dbCallback: AppDatabase.ZodiacDatabaseCallback
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "soulstone_database"
        )
            .fallbackToDestructiveMigration()
            // 2. THIS IS THE CRITICAL FIX: Add the callback
            .addCallback(dbCallback)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(
        @ApplicationContext context: Context,
        // Give the callback a *Provider* of the database
        // This lazily provides the DB and breaks the dependency cycle.
        dbProvider: Provider<AppDatabase>
    ): AppDatabase.ZodiacDatabaseCallback {
        return AppDatabase.ZodiacDatabaseCallback(context, dbProvider)
    }

    /**
     * Provides the [StoneDao] from the AppDatabase.
     */
    @Provides
    @Singleton
    fun provideStoneDao(db: AppDatabase): StoneDao {
        return db.stoneDao()
    }

    /**
     * Provides the [ZodiacSignDao] from the AppDatabase.
     * This is the one that fixes your error.
     */
    @Provides
    @Singleton
    fun provideZodiacSignDao(db: AppDatabase): ZodiacSignDao {
        return db.zodiacSignDao()
    }

    /**
     * Provides the [BenefitDao] from the AppDatabase.
     */
    @Provides
    @Singleton
    fun provideBenefitDao(db: AppDatabase): BenefitDao {
        return db.benefitDao()
    }

    /**
     * Provides the [ChakraDao] from the AppDatabase.
     */
    @Provides
    @Singleton
    fun provideChakraDao(db: AppDatabase): ChakraDao {
        return db.chakraDao()
    }

    /**
     * Provides the [ChineseZodiacSignDao] from the AppDatabase.
     */
    @Provides
    @Singleton
    fun provideChineseZodiacSignDao(db: AppDatabase): ChineseZodiacSignDao {
        return db.chineseZodiacSignDao()
    }
}