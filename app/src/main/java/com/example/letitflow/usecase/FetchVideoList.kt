package com.example.letitflow.usecase

import com.example.letitflow.model.VideoItem
import kotlinx.coroutines.flow.Flow

interface FetchVideoList {
    suspend fun execute(): Flow<List<VideoItem>>
}
