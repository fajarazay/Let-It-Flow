package com.example.letitflow.videoList

sealed class VideoListViewEffect {

    data class ShowToast(val message: String) : VideoListViewEffect()
}
