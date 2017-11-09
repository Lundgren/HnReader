package io.lundgren.hnreader.presentation.mvp.list

import io.lundgren.hnreader.domain.interactors.models.StoryItem

interface StoryListView {
    fun displayStories(stories: List<StoryItem>)
    fun displayLoading()
    fun hideLoading()
    fun displayError()
    fun startBrowser(url: String)
}