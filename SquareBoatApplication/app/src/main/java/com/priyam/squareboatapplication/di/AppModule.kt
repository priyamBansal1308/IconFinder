package com.priyam.squareboatapplication.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.priyam.squareboatapplication.network.IconFinderApi
import com.priyam.squareboatapplication.util.Constants

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class AppModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100,TimeUnit.SECONDS).addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(Constants.baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideFleetApi(retrofit: Retrofit): IconFinderApi {
        return retrofit.create(IconFinderApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultSharedPreferenceInstance(application:Application):SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(
            application
        )
    }




}