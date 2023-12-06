package com.assignment.currency.data.remote

import com.assignment.currency.data.local.entity.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiGateway {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") apiKey: String = API_KEY
    ): CurrencyResponse

    companion object {
        const val API_KEY = "cb0e01b74eb9022c36703c6155e0ef23"
        const val BASE_URL = "http://api.exchangeratesapi.io/v1/"
    }

}