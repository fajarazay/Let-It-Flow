package com.example.letitflow.repository

import com.example.letitflow.model.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun fetchVideoList(): Flow<List<VideoItem>>
}
