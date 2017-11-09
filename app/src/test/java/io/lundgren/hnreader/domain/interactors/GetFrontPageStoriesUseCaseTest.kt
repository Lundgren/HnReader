package io.lundgren.hnreader.domain.interactors

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.lundgren.hnreader.domain.interactors.models.StoryItem
import io.lundgren.hnreader.domain.models.Story
import io.lundgren.hnreader.domain.repositories.StoryRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.CompletableFuture

class GetFrontPageStoriesUseCaseTest {

    @Test
    fun execute() {
        // given:
        val story = Story(123, "title", "url", "user", 1, 2, 3)
        val expected = StoryItem(story.title, story.url, "https://news.ycombinator.com/item?id=${story.id}", story.user, story.score, story.commentsCount, story.time)
        val repo = mock<StoryRepository> {
            on { getFrontPageStories(any()) } doReturn Single.just(listOf(story))
        }
        val useCase = GetFrontPageStoriesUseCase(repo, Schedulers.single(), Schedulers.single())
        val future = CompletableFuture<List<StoryItem>>()

        // when:
        useCase.execute({ future.complete(it) })

        // then:
        assertEquals(future.get(), listOf(expected))
    }

    @Test
    fun execute_forceFreshData() {
        // given:
        val repo = mock<StoryRepository> {
            on { getFrontPageStories(any()) } doReturn Single.just(emptyList())
        }
        val useCase = GetFrontPageStoriesUseCase(repo, Schedulers.single(), Schedulers.single())

        // when:
        useCase.execute({ }, forceFresh = true)

        // then:
        verify(repo).getFrontPageStories(true)
    }

    @Test
    fun execute_onErrorOnException() {
        // given:
        val exception = RuntimeException("Some error")
        val repo = mock<StoryRepository> {
            on { getFrontPageStories(any()) } doReturn Single.error(exception)
        }
        val useCase = GetFrontPageStoriesUseCase(repo, Schedulers.single(), Schedulers.single())
        val future = CompletableFuture<Throwable>()

        // when:
        useCase.execute({}, { future.complete(it) })

        // then:
        assertEquals(future.get(), exception)
    }
}