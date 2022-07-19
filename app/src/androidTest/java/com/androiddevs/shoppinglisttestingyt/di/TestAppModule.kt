package com.androiddevs.shoppinglisttestingyt.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {


    @Provides
    @Named("test_db")
    fun providesInMemoryDB(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, ShoppingItemDatabase::class.java)
        .allowMainThreadQueries()
        .build()

}