package com.example.letitflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letitflow.usecase.FetchVideoList
import com.example.letitflow.videoList.VideoAction
import com.example.letitflow.videoList.VideoIntent
import com.example.letitflow.videoList.VideoResult
import com.example.letitflow.videoList.VideoViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel(
    fetchVideoList: FetchVideoList
) : ViewModel() {
    private val intentFlow = MutableSharedFlow<VideoIntent>()
    private val _uiState = MutableStateFlow(VideoViewState.idle())

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<VideoViewState> = _uiState

    private val intentFilter = {incomingFlow: Flow<VideoIntent> ->
        val sharedFlow = incomingFlow.shareIn(viewModelScope, SharingStarted.Eagerly)
        merge(
            sharedFlow.filter {it is VideoIntent.InitialLoadVideoListIntent},
            sharedFlow.filter {it is VideoIntent.RefreshVideoListIntent}
        )
    }

    private val intentToAction = {incomingFlow: Flow<VideoIntent> ->
        incomingFlow.map {intent ->
            when (intent) {
                is VideoIntent.RefreshVideoListIntent -> VideoAction.RefreshVideoListAction
                is VideoIntent.InitialLoadVideoListIntent -> VideoAction.InitialLoadVideoListAction
            }
        }
    }

    private val actionProcessor = {incomingFlow: Flow<VideoAction> ->
        val sharedFlow = incomingFlow.shareIn(viewModelScope, SharingStarted.Eagerly)

        val actionInitialLoad = {actionFlow: Flow<VideoAction.InitialLoadVideoListAction> ->
            actionFlow.flatMapConcat {
                flow<VideoResult> {
                    emitAll(
                        fetchVideoList.execute()
                            .map {VideoResult.InitialLoadVideoResult.Success(it)})
                }.catch {
                    emit(VideoResult.InitialLoadVideoResult.Error(it))
                }
                    .onStart {
                        emit(VideoResult.InitialLoadVideoResult.Loading)
                    }
                    .flowOn(Dispatchers.IO)
            }
        }

        val actionRefresh = {actionFlow: Flow<VideoAction.RefreshVideoListAction> ->
            actionFlow.flatMapConcat {
                flow<VideoResult> {
                    try {
                        emitAll(fetchVideoList.execute().map {
                            (VideoResult.RefreshLoadVideoResult.Success(it))
                        })
                    } catch (error: Throwable) {
                        emit(VideoResult.RefreshLoadVideoResult.Error(error))
                    }
                }
                    .onStart {
                        emit(VideoResult.RefreshLoadVideoResult.Loading as VideoResult)
                    }.flowOn(Dispatchers.IO)
            }
        }

        merge(
            sharedFlow.filter {it is VideoAction.InitialLoadVideoListAction}
                .map {it as VideoAction.InitialLoadVideoListAction}
                .let(actionInitialLoad),
            sharedFlow.filter {it is VideoAction.RefreshVideoListAction}
                .map {it as VideoAction.RefreshVideoListAction}
                .let(actionRefresh),
        )
    }

    private val reducer = {resultFlow: Flow<VideoResult> ->
        resultFlow.scan(VideoViewState.idle()) {previousState, result ->
            when (result) {
                is VideoResult.RefreshLoadVideoResult.Loading -> {
                    previousState.copy(
                        isLoading = true,
                        error = null,
                        renderState = getRenderViewState(
                            true,
                            null,
                            previousState.videoList.isEmpty()
                        )
                    )
                }
                is VideoResult.RefreshLoadVideoResult.Success -> {
                    previousState.copy(
                        isLoading = false,
                        error = null,
                        videoList = result.videoList,
                        renderState = getRenderViewState(false, null, result.videoList.isEmpty())
                    )
                }
                is VideoResult.RefreshLoadVideoResult.Error -> {
                    previousState.copy(
                        isLoading = false,
                        error = result.throwable,
                        renderState = getRenderViewState(
                            false,
                            result.throwable,
                            previousState.videoList.isEmpty()
                        )
                    )
                }

                is VideoResult.InitialLoadVideoResult.Error -> {
                    previousState.copy(
                        isLoading = false,
                        error = result.throwable,
                        renderState = getRenderViewState(
                            false,
                            result.throwable,
                            previousState.videoList.isEmpty()
                        )
                    )
                }

                is VideoResult.InitialLoadVideoResult.Loading -> {
                    previousState.copy(
                        isLoading = true,
                        error = null,
                        renderState = getRenderViewState(
                            true,
                            null,
                            previousState.videoList.isEmpty()
                        )
                    )
                }

                is VideoResult.InitialLoadVideoResult.Success -> {
                    previousState.copy(
                        isLoading = false,
                        error = null,
                        videoList = result.videoList,
                        renderState = getRenderViewState(false, null, result.videoList.isEmpty())
                    )
                }
            }
        }
    }

    init {
        intentFlow
            .let(intentFilter)
            .let(intentToAction)
            .let(actionProcessor)
            .let(reducer)
            .onEach {newState ->
                _uiState.value = newState
            }
            .launchIn(viewModelScope)
    }

    suspend fun doRefreshVideoList() {
        intentFlow.emit(VideoIntent.RefreshVideoListIntent)
    }

    suspend fun doInitialLoadVideoList() {
        intentFlow.emit(VideoIntent.InitialLoadVideoListIntent)
    }

    private fun getRenderViewState(
        isLoading: Boolean,
        error: Throwable?,
        isDataEmpty: Boolean
    ): VideoViewState.RenderViewState {
        return if (isLoading && error == null) {
            VideoViewState.RenderViewState.SHOW_LOADING
        } else if (error != null && !isLoading) {
            VideoViewState.RenderViewState.SHOW_ERROR
        } else if (!isDataEmpty && error == null) {
            VideoViewState.RenderViewState.SHOW_DATA
        } else {
            VideoViewState.RenderViewState.DO_NOTHING
        }
    }
}
