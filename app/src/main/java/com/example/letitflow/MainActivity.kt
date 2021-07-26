package com.example.letitflow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letitflow.databinding.ActivityMainBinding
import com.example.letitflow.videoList.VideoAdapter
import com.example.letitflow.videoList.VideoViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var videoAdapter: VideoAdapter

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoAdapter = VideoAdapter()

        with(binding.rvVideo) {
            layoutManager = LinearLayoutManager(context)
            adapter = videoAdapter
            setHasFixedSize(true)
        }

        binding.buttonRetry.setOnClickListener {
            lifecycleScope.launch {
                viewModel.doRefreshVideoList()
            }
        }

        viewModel = ViewModelProvider(
            this@MainActivity,
            DummyViewModelFactory()
        ).get(MainViewModel::class.java)

        viewModel.uiState.asLiveData().observe(this@MainActivity, {state ->
            render(state)
        })

//        lifecycleScope.launch {
//            viewModel.uiState.collect { state ->
//                render(state)
//            }
//        }

        lifecycleScope.launchWhenResumed {
            viewModel.doInitialLoadVideoList()
        }
    }


    private fun render(state: VideoViewState) {
        when (state.renderState) {
            VideoViewState.RenderViewState.DO_NOTHING -> {

            }
            VideoViewState.RenderViewState.SHOW_LOADING -> {
                with(binding) {
                    progressbar.visibility = View.VISIBLE
                    rvVideo.visibility = View.GONE
                    errorTv.visibility = View.GONE
                    buttonRetry.visibility = View.GONE
                }
            }

            VideoViewState.RenderViewState.SHOW_DATA -> {
                with(binding) {
                    progressbar.visibility = View.GONE
                    rvVideo.visibility = View.VISIBLE
                    errorTv.visibility = View.GONE
                    buttonRetry.visibility = View.GONE
                    videoAdapter.setData(state.videoList)
                    videoAdapter.notifyDataSetChanged()
                }
            }
            VideoViewState.RenderViewState.SHOW_ERROR -> {
                with(binding) {
                    progressbar.visibility = View.GONE
                    rvVideo.visibility = View.GONE
                    errorTv.visibility = View.VISIBLE
                    errorTv.text = state.error?.message
                    buttonRetry.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
