package com.example.letitflow.videoList

import com.example.letitflow.model.VideoItem

sealed class VideoResult {
    sealed class RefreshLoadVideoResult : VideoResult() {
        object Loading : RefreshLoadVideoResult()

        data class Success(val videoList: List<VideoItem>) : RefreshLoadVideoResult()

        data class Error(val throwable: Throwable) : RefreshLoadVideoResult()
    }

    sealed class InitialLoadVideoResult : VideoResult() {
        object Loading : InitialLoadVideoResult()

        data class Success(val videoList: List<VideoItem>) : InitialLoadVideoResult()

        data class Error(val throwable: Throwable) : InitialLoadVideoResult()
    }
}
