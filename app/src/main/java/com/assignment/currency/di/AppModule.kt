package com.assignment.currency.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.assignment.currency.data.local.CurrencyRateDatabase
import com.assignment.currency.data.local.DbHelper
import com.assignment.currency.data.local.DbHelperImpl
import com.assignment.currency.data.remote.ApiGateway
import com.assignment.currency.data.repository.CurrencyRepositoryImpl
import com.assignment.currency.domain.repository.CurrencyRepository
import com.currency.data.DataManager
import com.currency.data.DataManagerImpl
import com.currency.data.local.prefs.PreferencesHelper
import com.currency.data.local.prefs.PreferencesHelperImpl
import com.currency.utils.PREF_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)

abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideDataManager(dataManager: DataManagerImpl): DataManager

    @Binds
    @Singleton
    abstract fun provideDbHelper(dbHelper: DbHelperImpl): DbHelper

    @Binds
    @Singleton
    abstract fun providePreferencesHelper(preferencesHelper: PreferencesHelperImpl): PreferencesHelper


    companion object {

        private const val DEFAULT_TIMEOUT = 30L

        @Provides
        @Singleton
        fun provideContext(application: Application): Context {
            return application
        }

        @Provides
        @PreferenceInfo
        fun providePreferenceName(): String {
            return PREF_NAME
        }

        @Provides
        @Singleton
        fun provideCurrencyApi(): ApiGateway {

            val retrofit = Retrofit
                .Builder()
                .baseUrl(ApiGateway.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHTTPClient())
                .build()
            return retrofit.create(ApiGateway::class.java)
        }

        @Provides
        @Singleton
        fun provideDatabase(application: Application): CurrencyRateDatabase {
            return Room
                .databaseBuilder(
                    application,
                    CurrencyRateDatabase::class.java,
                    "currency_db"
                )
                .build()
        }

        @Provides
        @Singleton
        fun provideRepository(
            api: ApiGateway,
            db: CurrencyRateDatabase
        ): CurrencyRepository {
            return CurrencyRepositoryImpl(
                api = api
            )
        }

        fun getHTTPClient(): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)

            return httpClient.build()
        }

    }
}