package io.lundgren.hnreader.data.repositories

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.lundgren.hnreader.data.cache.StoryCache
import io.lundgren.hnreader.data.net.RestApi
import io.lundgren.hnreader.domain.models.Story
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test
import java.util.*

class CachedStoryRepositoryTest {

    @Test
    fun getFrontPageStories() {
        // given:
        val fromCache = generateStoryList()
        val fromApi = generateStoryList()
        val cache = mock<StoryCache> {
            on { getFrontPageStories() } doReturn Observable.just(fromCache)
        }
        val api = mock<RestApi> {
            on { getFrontPageStories() } doReturn Observable.just(fromApi)
        }
        val storyRepository = CachedStoryRepository(api, cache)
        val testObserver = TestObserver<List<Story>>()

        // when:
        storyRepository.getFrontPageStories().subscribeWith(testObserver)

        // then:
        testObserver.assertResult(fromCache)
    }

    @Test
    fun getFrontPageStories_takeFromCacheNoApi() {
        // given:
        val fromCache = generateStoryList()
        val cache = mock<StoryCache> {
            on { getFrontPageStories() } doReturn Observable.just(fromCache)
        }
        val api = mock<RestApi> {
            on { getFrontPageStories() } doReturn Observable.just(generateStoryList()).doOnNext { throw RuntimeException() }
        }
        val storyRepository = CachedStoryRepository(api, cache)

        // when:
        val testObserver = storyRepository.getFrontPageStories().test()

        // then:
        testObserver.assertResult(fromCache)
        testObserver.assertNoErrors()
    }

    @Test
    fun getFrontPageStories_takeFromApiWhenCacheReturnsEmpty() {
        // given:
        val cache = mock<StoryCache> {
            on { getFrontPageStories() } doReturn Observable.empty()
        }
        val fromApi = generateStoryList()
        val api = mock<RestApi> {
            on { getFrontPageStories() } doReturn Observable.just(fromApi)
        }
        val storyRepository = CachedStoryRepository(api, cache)

        // when:
        val testObserver = storyRepository.getFrontPageStories().test()

        // then:
        testObserver.assertResult(fromApi)
        testObserver.assertNoErrors()
    }

    @Test
    fun getFrontPageStories_storeInCacheAfterApi() {
        // given:
        val cache = mock<StoryCache> {
            on { getFrontPageStories() } doReturn Observable.empty()
        }
        val fromApi = generateStoryList()
        val api = mock<RestApi> {
            on { getFrontPageStories() } doReturn Observable.just(fromApi)
        }
        val storyRepository = CachedStoryRepository(api, cache)

        // when:
        storyRepository.getFrontPageStories().test()

        // then:
        verify(cache).setFrontPageStories(fromApi)
    }

    private fun generateStoryList(): List<Story> {
        return listOf(Story(Random().nextLong(), "title", "url", "user", 1, 1, 0))
    }
}