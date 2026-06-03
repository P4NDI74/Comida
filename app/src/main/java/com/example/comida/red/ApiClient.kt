package com.example.comida.red

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val clienteHttp = OkHttpClient.Builder()
        .build()

    val api: ComidaApi = Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .client(clienteHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ComidaApi::class.java)
}