package com.byandev.storyapp.di

import com.byandev.storyapp.BuildConfig
import com.byandev.storyapp.services.ApiServices
import com.byandev.storyapp.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideClientHttp(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        utilsConnect: UtilsConnect,
        sharedPrefManager: SharedPrefManager
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                val tokenAuth = "Bearer ${sharedPrefManager.token}"
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
                    if (sharedPrefManager.isRemember)
                        sharedPrefManager.logoutRemoveToken()
                    else sharedPrefManager.logoutNotRemember()
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

//    @Singleton
//    @Provides
//    fun moshi() : Moshi {
//        return Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()
//    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiServices(retrofit: Retrofit) : ApiServices {
        return retrofit.create(ApiServices::class.java)
    }


}