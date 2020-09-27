package com.example.exchangetest.network

import com.google.gson.annotations.SerializedName

data class OkResponse(val httpCode: Int = 200)

data class ErrorResponse(
    @SerializedName("errorCode")
    val errorCode: String,

    @SerializedName("message")
    val message: String
)

data class ExchangeResponse(

    // 조회 시간
    @SerializedName("timestamp")
    val timestamp: Long,

    // 기준 국가 화폐 (USD)
    @SerializedName("source")
    val source: String,

    // 국가별 환율 리스트
    @SerializedName("quotes")
    val quotes: Quotes
)

data class Quotes (

    @SerializedName("USDKRW")
    val USDKRW: String,

    @SerializedName("USDJPY")
    val USDJPY: String,

    @SerializedName("USDPHP")
    val USDPHP: String
)