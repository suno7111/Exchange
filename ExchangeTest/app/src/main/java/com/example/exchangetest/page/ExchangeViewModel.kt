package com.example.exchangetest.page

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import com.example.exchangetest.CommonData
import com.example.exchangetest.network.ExchangeResponse
import com.example.exchangetest.network.Quotes
import com.example.exchangetest.network.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application

    lateinit var lifecycleOwner: LifecycleOwner

    // 현재 선택 수취국가
    var currentReceiveCounty = CommonData.KOREA

    // 현재선택 국가환율
    var currentExchangeRate = 0.0

    // 현재 입력 송금액
    var currentAmount = 0

    // api조회시간
    var timestamp = 0L

    // api 국가별 환율리스트
    var quotes: Quotes? = null

    fun getExchange (callback: (success: Boolean) -> Unit) {

        RetrofitUtil.exchangeInterface.getExchange(
            CommonData.REQUEST_API_KEY,
            CommonData.REQUEST_CURRENCY,
            CommonData.REQUEST_SOURCE,
            CommonData.REQUEST_FORMAT).enqueue( object: Callback<ExchangeResponse> {

            override fun onResponse(
                call: Call<ExchangeResponse>,
                response: Response<ExchangeResponse>
            ) {
                response.body()?.let {model ->
                    timestamp = model.timestamp * 1000L
                    quotes = model.quotes
                }
                callback.invoke(true)
            }

            override fun onFailure(call: Call<ExchangeResponse>, t: Throwable) {
                Log.d("getExchange", t.message.toString())
                callback.invoke(false)
            }
        })
    }
}