package com.example.aifavs

import com.example.aifavs.SharedPrefHelper.get
import com.example.aifavs.SharedPrefHelper.set
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private val retrofit: Retrofit by lazy {
        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        builder.client(createOkHttpClient()).build()
    }

    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)

    private fun createSignUpRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().build()) // 不添加拦截器
            .build()
    }

    fun createOkHttpClient(): OkHttpClient {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 8
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val sseSkippingInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (response.header("Content-Type")?.contains("text/event-stream") == true) {
                response
            } else {
                httpLoggingInterceptor.intercept(chain)
            }
        }

        return OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val pref = SharedPrefHelper.getPref(App.context)
                var userId: String? = pref["user_id"]
                if (userId?.isEmpty() == true) {
                    val remoteApi = createSignUpRetrofit().create(RemoteApi::class.java)
                    val signUpResponse = remoteApi.signUp().blockingFirst()
                    if (signUpResponse.isSuccess()) {
                        userId = signUpResponse.data?.id
                        pref["user_id"] = userId
                    } else {
                        throw IOException("Signup failed with code ${signUpResponse.code}")
                    }
                }
                val newRequest = chain.request().newBuilder()
                    .addHeader("X-USER-ID", userId!!)
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(sseSkippingInterceptor)
            .dispatcher(dispatcher)
            .build()
    }
}