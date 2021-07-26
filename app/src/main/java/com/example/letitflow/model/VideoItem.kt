package com.example.letitflow.model

data class VideoItem(
    val description: String,
    val presenter_name: String,
    val thumbnail_url: String,
    val title: String,
    val video_duration: Int,
    val video_url: String
) {
    companion object {
        fun createDummyVideoItem(): VideoItem {
            return VideoItem(
                "asdasd",
                "abcd",
                "werrtty",
                "tjkrtj",
                1000,
                "opioipop"
            )
        }
    }
}
