package com.example.letitflow.usecase

import app.cash.turbine.test
import com.example.letitflow.model.VideoItem
import com.example.letitflow.repository.VideoRepository
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
class FetchVideoListImplTest {

    @Mock
    private lateinit var mockedVideoRepository: VideoRepository

    private lateinit var fetchVideoList: FetchVideoList

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        fetchVideoList = FetchVideoListImpl(mockedVideoRepository)
    }

    @Test
    fun `fetchVideoList should return video list when success`() = runBlocking {
        val expectedVideoList = listOf(VideoItem.createDummyVideoItem())

        whenever(mockedVideoRepository.fetchVideoList()).thenReturn(flowOf(expectedVideoList))

        fetchVideoList.execute().test {
            assertEquals(expectedVideoList, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `fetchVideoList should emit throwable when failed`() = runBlocking {
        val expectedError = Throwable()

        whenever(mockedVideoRepository.fetchVideoList()).thenReturn(flow {throw expectedError})

        fetchVideoList.execute().test {
            assertEquals(expectedError.message, awaitError().message)
        }
    }
}
