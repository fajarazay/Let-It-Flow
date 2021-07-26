package com.example.letitflow.usecase

import com.example.letitflow.model.VideoItem
import com.example.letitflow.repository.VideoRepository
import kotlinx.coroutines.flow.Flow

class FetchVideoListImpl(private val videoRepository: VideoRepository) : FetchVideoList {
    override suspend fun execute(): Flow<List<VideoItem>> {
        return videoRepository.fetchVideoList()
    }
}
