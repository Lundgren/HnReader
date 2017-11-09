package io.lundgren.hnreader.data.repositories

import io.lundgren.hnreader.data.cache.StoryCache
import io.lundgren.hnreader.data.net.RestApi
import io.lundgren.hnreader.domain.models.Story
import io.lundgren.hnreader.domain.repositories.StoryRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CachedStoryRepository @Inject constructor(restApi: RestApi, storyCache: StoryCache): StoryRepository {

    private val cacheOrApi: Single<List<Story>>
    private val apiDirectly: Single<List<Story>>

    init {
        val apiWithWriteToCache = restApi
                .getFrontPageStories()
                .doOnNext { storyCache.setFrontPageStories(it) }
        val fromCache = storyCache.getFrontPageStories()

        cacheOrApi = Observable.concat(fromCache, apiWithWriteToCache).firstOrError()
        apiDirectly = apiWithWriteToCache.firstOrError()
    }

    override fun getFrontPageStories(forceFreshData: Boolean): Single<List<Story>> {
        if (forceFreshData) {
            return apiDirectly
        }

        return cacheOrApi
    }
}