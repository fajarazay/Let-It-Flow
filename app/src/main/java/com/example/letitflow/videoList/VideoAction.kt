package com.example.letitflow.videoList

sealed class VideoAction {
    object RefreshVideoListAction : VideoAction()
    object InitialLoadVideoListAction : VideoAction()
}
