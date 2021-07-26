package com.example.letitflow.repository

import com.example.letitflow.datasource.VideoService
import com.example.letitflow.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class VideoRepositoryImpl(private val videoService: VideoService) : VideoRepository {
    override suspend fun fetchVideoList(): Flow<List<VideoItem>> {
        return flow {
            val response = videoService.fetchVideoList()
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}
