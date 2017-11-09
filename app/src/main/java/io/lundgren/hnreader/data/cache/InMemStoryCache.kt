package io.lundgren.hnreader.data.cache

import io.lundgren.hnreader.domain.models.Story
import io.reactivex.Observable

class InMemStoryCache constructor(private val maxAge: Long) : StoryCache {

    private var frontPageCache: CacheEntry<List<Story>>? = null

    override fun getFrontPageStories(): Observable<List<Story>> {
        return frontPageCache.toObservable()
    }

    override fun setFrontPageStories(stories: List<Story>) {
        frontPageCache = CacheEntry(stories)
    }


    private data class CacheEntry<out T>(val item: T, val time: Long = System.currentTimeMillis())

    private fun <T> CacheEntry<T>?.toObservable(): Observable<T> {
        if (this == null || this.isStale()) {
            return Observable.empty()
        }

        return Observable.just(item)
    }

    private fun CacheEntry<*>.isStale(): Boolean {
        return System.currentTimeMillis() > time + maxAge
    }
}