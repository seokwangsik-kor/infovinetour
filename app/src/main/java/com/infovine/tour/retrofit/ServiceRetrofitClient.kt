package com.infovine.tour.retrofit

import com.infovine.tour.utils.BaseUtil
import com.infovine.tour.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ServiceRetrofitClient {

    private var instance: ServiceRetrofitClient? = null
    private var retrofitApi: RetrofitAPI

    fun getInstance() : ServiceRetrofitClient? {
        if (instance == null) {
            instance = ServiceRetrofitClient()
        }
        return instance
    }

    init {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient
        if(BaseUtil.isDebug){
            client = OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .build()
        }else{
            client = OkHttpClient().newBuilder()
                .build()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(Const.SERVICE_DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofitApi = retrofit.create(RetrofitAPI::class.java)
    }

    fun getRetrofitInterface(): RetrofitAPI {
        return retrofitApi
    }

}