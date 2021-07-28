package com.example.letitflow.repository

import app.cash.turbine.test
import com.example.letitflow.datasource.VideoService
import com.example.letitflow.model.VideoItem
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalTime
@ExperimentalCoroutinesApi
class VideoRepositoryImplTest {
    private lateinit var videoRepository: VideoRepository

    @Mock
    private lateinit var mockedVideoService: VideoService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        videoRepository = VideoRepositoryImpl(mockedVideoService)
    }

    @Test
    fun `fetchVideoList should return video list when success`() = runBlocking{
        val expectedVideoList = listOf(VideoItem.createDummyVideoItem())

        whenever(mockedVideoService.fetchVideoList()).thenReturn(expectedVideoList)

        videoRepository.fetchVideoList().test {
            assertEquals(expectedVideoList, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `fetchVideoList should emit throwable when failed`() = runBlocking{
        val expectedError = Throwable()

        whenever(mockedVideoService.fetchVideoList()).thenAnswer {throw expectedError}

        videoRepository.fetchVideoList().test {
            assertEquals(expectedError.message, awaitError().message)
        }
    }
}
