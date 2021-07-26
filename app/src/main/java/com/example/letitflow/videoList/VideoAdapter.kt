package com.example.letitflow.videoList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.letitflow.databinding.RowVideoItemBinding
import com.example.letitflow.model.VideoItem

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    private lateinit var listData: List<VideoItem>

    fun setData(videoList: List<VideoItem>) {
        this.listData = videoList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.ViewHolder {
        val binding: RowVideoItemBinding =
            RowVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        holder.binding(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ViewHolder(private val binding: RowVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binding(data: VideoItem) {
            binding.tvPresenterName.text = data.presenter_name
            binding.tvTitle.text = data.title

            Glide.with(itemView.context).load(data.thumbnail_url).into(binding.ivVideo)
        }
    }
}
