package com.example.letitflow.datasource

import com.example.letitflow.model.VideoItem
import retrofit2.http.GET

interface VideoService {

    @GET("native-technical-exam/playlist.json")
    suspend fun fetchVideoList(): List<VideoItem>
}
