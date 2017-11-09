package io.lundgren.hnreader.domain.interactors.models

data class StoryItem(
        val title: String,
        val url: String,
        val commentsUrl: String,
        val user: String,
        val score: Int,
        val commentsCount: Int,
        val time: Long
)