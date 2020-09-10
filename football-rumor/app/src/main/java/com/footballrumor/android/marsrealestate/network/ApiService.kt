package com.footballrumor.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query




val retrofit = Retrofit.Builder()
        .baseUrl("http://128.199.183.164:8081/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


interface FootballApiservice {
    @GET("api/v1/sports")
    fun getdata(@Query("pageSize") pageSize: Int, @Query("page") page: Int,@Header("Authorization") authHeader : String): Call<FootballList>
}

object FootballApi{
    val retrofitService : FootballApiservice by lazy { retrofit.create(FootballApiservice::class.java) }
}
