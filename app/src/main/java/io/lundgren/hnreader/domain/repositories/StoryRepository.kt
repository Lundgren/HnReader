package io.lundgren.hnreader.domain.repositories

import io.lundgren.hnreader.domain.models.Story
import io.reactivex.Single

interface StoryRepository {
    fun getFrontPageStories(forceFreshData: Boolean = false): Single<List<Story>>
}