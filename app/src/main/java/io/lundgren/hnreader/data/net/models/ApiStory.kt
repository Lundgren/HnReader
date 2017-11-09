package io.lundgren.hnreader.data.net.models

data class ApiStory(
        val id: Long,
        val title: String,
        val points: Int,
        val user: String?,
        val time: Long,
        val time_ago: String,
        val comments_count: Int,
        val type: String,
        val url: String,
        val domain: String
)