package io.lundgren.hnreader.domain.models

data class Story(
        val id: Long,
        val title: String,
        val url: String,
        val user: String,
        val score: Int,
        val commentsCount: Int,
        val time: Long
)