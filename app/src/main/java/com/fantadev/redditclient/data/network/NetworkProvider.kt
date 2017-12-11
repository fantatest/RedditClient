package com.fantadev.redditclient.data.network

import com.fantadev.redditclient.presentation.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object NetworkProvider {

    fun provideOkHttp() = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    fun provideGson() = GsonBuilder()
            .create()

    fun provideRetrofit(gson: Gson = provideGson(), okHttpClient: OkHttpClient = provideOkHttp()): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.baseUrl)
                .client(okHttpClient)
                .build()
    }
}