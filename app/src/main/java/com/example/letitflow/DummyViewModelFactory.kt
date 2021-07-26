package com.example.letitflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.letitflow.datasource.RetrofitBuilder
import com.example.letitflow.repository.VideoRepositoryImpl
import com.example.letitflow.usecase.FetchVideoListImpl

class DummyViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {

            val videoService = RetrofitBuilder.getVideoService()
            val videoRepository = VideoRepositoryImpl(videoService)
            val fetchVideoList = FetchVideoListImpl(videoRepository)

            return MainViewModel(fetchVideoList) as T
        } else {
            throw Throwable()
        }
    }
}
