package com.example.letitflow

import app.cash.turbine.test
import com.example.letitflow.model.VideoItem
import com.example.letitflow.usecase.FetchVideoList
import com.example.letitflow.videoList.VideoViewState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalTime
@ExperimentalCoroutinesApi
class MainViewModelTest : CoroutineTest{

    override lateinit var testScope: TestCoroutineScope
    override lateinit var dispatcher: TestCoroutineDispatcher

    @MockK
    private lateinit var mockedFetchVideoList: FetchVideoList

    private lateinit var mainViewModel: MainViewModel

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(mockedFetchVideoList)
    }

    @DisplayName("Given Nothing")
    @Nested
    inner class DefaultStateTest {

        @DisplayName("When not receive any intent")
        @Nested
        inner class NoState {

            @Test
            @DisplayName("then should emit Default State")
             fun thenCondition() = runBlockingTest{
               // mainViewModel.doInitialLoadVideoList()
                mainViewModel.uiState.test {
                    assertEquals(VideoViewState.idle(), awaitItem())
                }
            }
        }
    }

    @DisplayName("Given InitialLoadVideoListIntent")
    @Nested
    inner class InitialLoadVideoListIntentTest {

        @DisplayName("When receive InitialLoadVideoListIntent")
        @Nested
        inner class NoState {
            @Test
            @DisplayName("then should emit InitialLoadVideoResult")
            fun thenCondition() = runBlocking{
                val expectedVideoList = listOf(VideoItem.createDummyVideoItem())
                coEvery {mockedFetchVideoList.execute()} returns flowOf(expectedVideoList)

                mainViewModel.uiState.test {
                    assertEquals(VideoViewState.idle(), awaitItem())
                    mainViewModel.doInitialLoadVideoList()
                    assertEquals(VideoViewState.RenderViewState.SHOW_LOADING, awaitItem().renderState)
                    assertEquals(expectedVideoList, awaitItem().videoList)
                }
            }
        }
    }

}
