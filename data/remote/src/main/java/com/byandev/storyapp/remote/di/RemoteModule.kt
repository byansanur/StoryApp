package com.byandev.storyapp.remote.di

import com.byandev.storyapp.common.SharedPrefManager
import com.byandev.storyapp.common.UtilsConnect
import com.byandev.storyapp.remote.BuildConfig
import com.byandev.storyapp.remote.datasource.RemoteDatasource
import com.byandev.storyapp.remote.services.ApiServices
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val remoteModule = module {
    factory<Interceptor> {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    factory {
        val sharedPref by inject<SharedPrefManager>()
        val utilsConnect by inject<UtilsConnect>()

        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                val tokenAuth = "Bearer ${sharedPref.token}"
                val req = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", tokenAuth)
                    .build()
                chain.proceed(req)
            }
            .addInterceptor { chain ->
                val req = chain.request()
                val res = chain.proceed(req)
                if (res.code == 401 || res.code == 403) {
                    if (sharedPref.isRemember)
                        sharedPref.logoutRemoveToken()
                    else sharedPref.logoutNotRemember()
                }
                res
            }
            .addInterceptor { chain ->
                //handle offline cache
                var request = chain.request()
                if (!utilsConnect.isConnectedToInternet()) {
                    request = request.newBuilder().cacheControl(
                        CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()
                    ).build()
                }
                chain.proceed(request)
            }
            .addInterceptor { chain ->
                // cache
                chain.proceed(
                    chain.request().newBuilder().cacheControl(
                        CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()
                    ).build()
                )
            }
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    factory { get<Retrofit>().create(ApiServices::class.java) }

    factory { RemoteDatasource(get()) }
}