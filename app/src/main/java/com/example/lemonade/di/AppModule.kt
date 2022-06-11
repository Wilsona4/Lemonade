package com.example.lemonade.di

import android.content.Context
import androidx.room.Room
import com.example.lemonade.data.local.StudentDao
import com.example.lemonade.data.local.StudentDatabase
import com.example.lemonade.data.local.StudentDatabase.Companion.DATABASE_NAME
import com.example.lemonade.data.remote.ApiService
import com.example.lemonade.data.repository.StudentRepositoryImpl
import com.example.lemonade.domain.repository.StudentRepository
import com.example.lemonade.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesStudentDataBase(@ApplicationContext context: Context): StudentDatabase {
        return Room.databaseBuilder(context, StudentDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesStudentDAO(database: StudentDatabase): StudentDao = database.studentDao()

    @Singleton
    @Provides
    fun provideStudentRepository(
        lemonadeApiService: ApiService,
        database: StudentDatabase,
        @Dispatcher(LemonadeDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): StudentRepository {
        return StudentRepositoryImpl(lemonadeApiService, database, ioDispatcher)
    }
}