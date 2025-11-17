package com.filip.movieexplorer.data.network

import com.filip.movieexplorer.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
     * Creates OkHttpClient with logging interceptor for debugging.
     */
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
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
    fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
            .build()
    }

    /**
     * Creates OMDb API service instance.
     */
    fun createApiService(): OmdbApiService {
        return createRetrofit().create(OmdbApiService::class.java)
    }
}

