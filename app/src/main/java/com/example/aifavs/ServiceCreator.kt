package com.example.aifavs

import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private val okHttpClient by lazy { OkHttpClient().newBuilder() }
    private val retrofit: Retrofit by lazy {
        val builder = Retrofit.Builder()
            .baseUrl("http://192.168.0.106:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .dispatcher(dispatcher)
        builder.client(okHttpClient.build()).build()
    }

    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)
}