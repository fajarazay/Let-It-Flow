package com.example.letitflow.datasource

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        fun getVideoService(): VideoService {
            val client: OkHttpClient = OkHttpClient.Builder().build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://quipper.github.io/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit.create(VideoService::class.java)
        }
    }
}
