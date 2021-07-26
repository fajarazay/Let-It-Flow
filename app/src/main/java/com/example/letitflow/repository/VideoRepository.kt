package com.example.letitflow.repository

import com.example.letitflow.model.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun fetchVideoList(): Flow<List<VideoItem>>
}
