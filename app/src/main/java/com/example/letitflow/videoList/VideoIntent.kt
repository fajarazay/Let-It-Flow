package com.example.letitflow.videoList

sealed class VideoIntent {
    object InitialLoadVideoListIntent : VideoIntent()
    object RefreshVideoListIntent : VideoIntent()
}
