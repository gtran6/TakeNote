package com.example.takenotecomposeapp.di

import android.content.Context
import androidx.room.Room
import com.example.takenotecomposeapp.data.TaskRepository
import com.example.takenotecomposeapp.data.TaskRepositoryImpl
import com.example.takenotecomposeapp.data.local.TaskDao
import com.example.takenotecomposeapp.data.local.TaskDatabase
import com.example.takenotecomposeapp.data.network.NetworkDataSource
import com.example.takenotecomposeapp.data.network.TaskNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repositoryImpl: TaskRepositoryImpl): TaskRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(taskNetworkDataSource: TaskNetworkDataSource): NetworkDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            "Tasks.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()
}