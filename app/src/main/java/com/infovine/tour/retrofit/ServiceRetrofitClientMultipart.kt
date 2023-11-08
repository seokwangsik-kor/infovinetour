package com.infovine.moneyget.retrofit

import com.infovine.tour.retrofit.RetrofitAPI
import com.infovine.tour.utils.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class ServiceRetrofitClientMultipart {

    private var instance: ServiceRetrofitClientMultipart? = null
    private var retrofitApi: RetrofitAPI

    fun getInstance() : ServiceRetrofitClientMultipart? {
        if (instance == null) {
            instance = ServiceRetrofitClientMultipart()
        }
        return instance
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Const.SERVICE_DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(AppInterceptor()))
            .build()

        retrofitApi = retrofit.create(RetrofitAPI::class.java)
    }

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .run {
                addInterceptor(interceptor)
                build()
            }
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Content-type", "multipart/form-data")
//                .addHeader("Content-type", "application/json")
                .build()

            proceed(newRequest)
        }
    }


    fun getRetrofitInterface(): RetrofitAPI {
        return retrofitApi
    }

}