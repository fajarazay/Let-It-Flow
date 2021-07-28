package com.example.letitflow.usecase

import com.example.letitflow.model.VideoItem
import kotlinx.coroutines.flow.Flow

interface FetchVideoList {
    fun execute(): Flow<List<VideoItem>>
}
