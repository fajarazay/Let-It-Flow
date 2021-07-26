package com.example.letitflow.datasource

import com.example.letitflow.model.VideoItem
import retrofit2.http.GET

interface VideoService {

    @GET("")
    suspend fun fetchVideoList(): List<VideoItem>
}
