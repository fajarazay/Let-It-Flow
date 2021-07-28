package com.example.letitflow

import app.cash.turbine.test
import com.example.letitflow.model.VideoItem
import com.example.letitflow.usecase.FetchVideoList
import com.example.letitflow.videoList.VideoViewState
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalTime
@ExperimentalCoroutinesApi
class MainViewModelTesting {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var mockedFetchVideoList: FetchVideoList

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(mockedFetchVideoList)
    }

    @Test
    fun `InitialLoadVideoListIntent should emit data when success`() = runBlocking {
        val expectedVideoList = listOf(VideoItem.createDummyVideoItem())

        whenever(mockedFetchVideoList.execute()).thenReturn(flowOf(expectedVideoList))

        viewModel.uiState.test {
            assertEquals(VideoViewState.idle(), awaitItem())
            viewModel.doInitialLoadVideoList()
            assertEquals(VideoViewState.RenderViewState.SHOW_LOADING, awaitItem().renderState)
            assertEquals(expectedVideoList, awaitItem().videoList)
        }
    }

    @Test
    fun `InitialLoadVideoListIntent should emit error data when failed`() =
        runBlocking {
            val expectedError = Throwable()

            whenever(mockedFetchVideoList.execute()).thenReturn(flow {throw expectedError})
            viewModel.uiState.test {
                assertEquals(VideoViewState.idle(), awaitItem())
                viewModel.doInitialLoadVideoList()
                assertEquals(VideoViewState.RenderViewState.SHOW_LOADING, awaitItem().renderState)
                assertEquals(expectedError, awaitItem().error)
            }
        }
}
