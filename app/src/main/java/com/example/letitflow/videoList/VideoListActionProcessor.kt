package com.example.letitflow.videoList

import com.example.letitflow.usecase.FetchVideoList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class VideoListActionProcessor(private val fetchVideoList: FetchVideoList) {

}
