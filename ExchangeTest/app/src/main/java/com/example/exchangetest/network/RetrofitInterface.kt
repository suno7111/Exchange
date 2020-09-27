package com.example.exchangetest.network

import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    // ex) http://apilayer.net/api/live?access_key=d54005471235822af3f2509866ccc0c5&currencies=KRW,JPY,PHP&source=USD&format=1
    @GET("/live")
    fun getExchange(
        @Query("access_key") access_key: String,
        @Query("currencies") currencies: String,
        @Query("source") source: String,
        @Query("format") format: String
    ): Call<ExchangeResponse>
}