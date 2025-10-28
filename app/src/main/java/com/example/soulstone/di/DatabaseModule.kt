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
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "soulstone_database" // Your database file name
        )
            .fallbackToDestructiveMigration() // Use this for development
            // .addMigrations(...) // Or this, for production
            .build()
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