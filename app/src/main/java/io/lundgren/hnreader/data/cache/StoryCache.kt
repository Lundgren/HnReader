package io.lundgren.hnreader.data.cache

import io.lundgren.hnreader.domain.models.Story
import io.reactivex.Observable

interface StoryCache {
    fun getFrontPageStories(): Observable<List<Story>>
    fun setFrontPageStories(stories: List<Story>)
}