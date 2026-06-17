package com.example.my_list_itens.di

import com.example.my_list_itens.data.local.database.AppDatabase
import com.example.my_list_itens.data.local.dao.itemDao.ItemDao
import com.example.my_list_itens.data.local.dao.historyDao.HistoryDao
import android.content.Context
import androidx.room.Room

import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "item_db"
        ).build()
    }

    @Provides
    fun provideItemDao(
        db: AppDatabase
    ): ItemDao {
        return db.itemDao()
    }

    @Provides
    fun provideHistoryDao(
        db : AppDatabase
    ) : HistoryDao {
        return  db.historyDao()
    }
}