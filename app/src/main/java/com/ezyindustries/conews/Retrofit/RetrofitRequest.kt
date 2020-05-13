package com.ezyindustries.conews.Retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun httpClient(): OkHttpClient {
    val logInterceptor = HttpLoggingInterceptor()
    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val builder = OkHttpClient.Builder()
    builder.readTimeout(20, TimeUnit.SECONDS)
    builder.connectTimeout(20, TimeUnit.SECONDS)
    builder.addInterceptor(logInterceptor)
    return builder.build()
}

inline fun <reified T> apiRequest(okHttpClient: OkHttpClient): T {
    val gson = GsonBuilder().create()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://conews.herokuapp.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    return retrofit.create(T::class.java)
}