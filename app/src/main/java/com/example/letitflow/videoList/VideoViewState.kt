package com.example.letitflow.videoList

import com.example.letitflow.model.VideoItem

data class VideoViewState(
    val isLoading: Boolean,
    val videoList: List<VideoItem>,
    val error: Throwable?,
    val renderState: RenderViewState
) {
    companion object {
        fun idle(): VideoViewState {
            return VideoViewState(
                isLoading = false,
                videoList = emptyList(),
                error = null,
                renderState = RenderViewState.DO_NOTHING
            )
        }
    }

    enum class RenderViewState {
        DO_NOTHING, SHOW_LOADING, SHOW_DATA, SHOW_ERROR
    }
}
