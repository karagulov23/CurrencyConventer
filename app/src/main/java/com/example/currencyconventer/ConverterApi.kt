package com.example.currencyconventer

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigDecimal
import java.util.concurrent.TimeUnit


object APIClient {

    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    val exchangeRatesApi: ExchangeRatesApi
        get() {
            val headersInterceptor = Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header("apikey", "JaHHVhALKKMRCfwZQyD73fFx2ffmg5Yr")
                chain.proceed(requestBuilder.build())
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logger)
                .addInterceptor(headersInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.apilayer.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ExchangeRatesApi::class.java)
        }
}

interface ExchangeRatesApi {

    data class ConvertResponse(
        val date: String,
        val info: Info,
        val result: BigDecimal,
        val success: Boolean
    ) {
        data class Info(val rate: BigDecimal, val timestamp: Long)

        override fun toString(): String {
            return "rate: ${info.rate}, timestamp: ${info.timestamp}, result: $result, success: $success"
        }
    }


    data class SymbolsResponse(
        val success: Boolean,
        val symbols: Map<String, String>
    )

    @GET("/exchangerates_data/convert")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): ConvertResponse

    @GET("/exchangerates_data/symbols")
    suspend fun getSymbols(): SymbolsResponse

}
