package com.filip.movieexplorer.data.network

import com.filip.movieexplorer.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Configuration and setup for OMDb API client.
 */
object OmdbApiConfig {
    const val BASE_URL = "https://www.omdbapi.com/"

    /**
     * Get API key from BuildConfig.
     * Replace YOUR_OMDB_API_KEY with your actual API key in build.gradle.kts
     */
    val API_KEY: String
        get() = BuildConfig.OMDB_API_KEY

    /**
     * Creates OkHttpClient with logging interceptor and HTTP cache for better performance.
     */
    private fun createOkHttpClient(context: Context? = null): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        // Add HTTP cache if context is provided
        context?.let {
            val cacheSize = 10 * 1024 * 1024L // 10 MB
            val cacheDir = File(it.cacheDir, "http_cache")
            val cache = Cache(cacheDir, cacheSize)
            builder.cache(cache)
        }

        return builder.build()
    }

    /**
     * Creates Moshi instance for JSON parsing.
     */
    private fun createMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * Creates Retrofit instance configured for OMDb API.
     */
    fun createRetrofit(context: Context? = null): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(context))
            .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
            .build()
    }

    /**
     * Creates OMDb API service instance.
     */
    fun createApiService(context: Context? = null): OmdbApiService {
        return createRetrofit(context).create(OmdbApiService::class.java)
    }
}

