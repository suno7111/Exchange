package com.example.exchangetest.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitUtil {
    // http://apilayer.net/api/live?access_key=d54005471235822af3f2509866ccc0c5&currencies=KRW,JPY,PHP&source=USD&format=1

    const val BASE_URL = "http://apilayer.net/api/"

    val exchangeInterface: RetrofitInterface
        get() = getClient(exchangeRetrofit)!!.create(RetrofitInterface::class.java)

    private var exchangeRetrofit: Retrofit? = null

    private fun getClient(retrofit: Retrofit?): Retrofit? {

        if (exchangeRetrofit == null) {
            exchangeRetrofit = createRetrofit(retrofit, BASE_URL)
        }
        return exchangeRetrofit
    }


    private fun createRetrofit(retrofit: Retrofit?, url: String): Retrofit {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                .build()
        }
        return retrofit
    }


}

//object ApiManager {
//
//    private val retrofitInterface: RetrofitInterface = RetrofitUtil.exchangeInterface
//
//    fun getExchange(
//        access_key: String,
//        currencies: String,
//        source: String,
//        format: String,
//        result: ApiResult<ExchangeResponse>
//    ) {
//        retrofitInterface.getExchange(
//            CommonData.EXCHANGE_API_KEY,
//            currencies,
//            source,
//            format
//        ).enqueue(newCallBack(result))
//    }
//
//    private fun newCallBack(result: ApiResult<ExchangeResponse>): Callback<ExchangeResponse> {
//
//        return object : Callback<ExchangeResponse> {
//            override fun onResponse(call: Call<ExchangeResponse>, response: Response<ExchangeResponse>) {
//                if (response.isSuccessful) {
//                    val returnModel = converter.invoke(response)
//                    if (returnModel == null) {
//
//                    } else {
//                        if (returnModel is OkResponse) {
//                            result.onResponse(OkResponse(response.code()) as R)
//                            return
//                        }
//                        result.onResponse(returnModel)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<ExchangeResponse>, t: Throwable) {}
//        }
//    }
//}