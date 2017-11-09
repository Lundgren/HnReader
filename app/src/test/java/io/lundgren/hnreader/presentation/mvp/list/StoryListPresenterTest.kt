package io.lundgren.hnreader.presentation.mvp.list

import com.nhaarman.mockito_kotlin.*
import io.lundgren.hnreader.domain.interactors.GetFrontPageStoriesUseCase
import io.lundgren.hnreader.domain.interactors.models.StoryItem
import org.junit.Test
import java.util.*

class StoryListPresenterTest {

    @Test
    fun onViewAttached() {
        // given:
        val someStories = listOf(randomStoryItem())
        val onSuccess = argumentCaptor<(List<StoryItem>) -> Unit>()
        val useCase = mock<GetFrontPageStoriesUseCase>()
        val view = mock<StoryListView>()
        val inOrder = inOrder(view)
        val presenter = StoryListPresenter(useCase)

        // when:
        presenter.onViewAttached(view)
        verify(useCase).execute(onSuccess.capture(), any(), eq(false))
        onSuccess.firstValue(someStories)

        // then:
        inOrder.verify(view).displayLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayStories(someStories)
    }

    @Test
    fun onViewAttached_onErrorIsCalledWhenFetchError() {
        // given:
        val onError = argumentCaptor<(Throwable) -> Unit>()
        val useCase = mock<GetFrontPageStoriesUseCase>()
        val view = mock<StoryListView>()
        val inOrder = inOrder(view)
        val presenter = StoryListPresenter(useCase)

        // when:
        presenter.onViewAttached(view)
        verify(useCase).execute(any(), onError.capture(), eq(false))
        onError.firstValue(Throwable())

        // then:
        inOrder.verify(view).displayLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayError()
    }

    @Test
    fun onViewDetach() {
        // given:
        val useCase = mock<GetFrontPageStoriesUseCase>()
        val view = mock<StoryListView>()
        val presenter = StoryListPresenter(useCase)

        presenter.onViewAttached(view)
        reset(view)

        // when:
        presenter.onViewDetach()

        // then:
        verify(useCase).dispose()
    }

    @Test
    fun onForceFetch() {
        // given:
        val someStories = listOf(randomStoryItem())
        val onSuccess = argumentCaptor<(List<StoryItem>) -> Unit>()
        val useCase = mock<GetFrontPageStoriesUseCase>()
        val view = mock<StoryListView>()
        val inOrder = inOrder(view)
        val presenter = StoryListPresenter(useCase)

        presenter.onViewAttached(view)
        reset(view)

        // when:
        presenter.onForceFetch()
        verify(useCase).execute(onSuccess.capture(), any(), eq(true))
        onSuccess.firstValue(someStories)

        // then:
        inOrder.verify(view).displayLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayStories(someStories)
    }

    @Test
    fun onForceFetch_onErrorIsCalledWhenFetchError() {
        // given:
        val onError = argumentCaptor<(Throwable) -> Unit>()
        val useCase = mock<GetFrontPageStoriesUseCase>()
        val view = mock<StoryListView>()
        val inOrder = inOrder(view)
        val presenter = StoryListPresenter(useCase)

        presenter.onViewAttached(view)
        reset(view)

        // when:
        presenter.onForceFetch()
        verify(useCase).execute(any(), onError.capture(), eq(true))
        onError.firstValue(Throwable())

        // then:
        inOrder.verify(view).displayLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).displayError()
    }

    @Test
    fun onStoryClicked() {
        // given:
        val view = mock<StoryListView>()
        val presenter = StoryListPresenter(mock())
        val url = "http://some.url/"
        presenter.onViewAttached(view)

        // when:
        presenter.onStoryClicked(StoryItem("", url, "", "", 0, 0, 0))

        // then:
        verify(view).startBrowser(url)
    }

    @Test
    fun onCommentClicked() {
        // given:
        val view = mock<StoryListView>()
        val presenter = StoryListPresenter(mock())
        val commentsUrl = "http://some.comment.url/"
        presenter.onViewAttached(view)

        // when:
        presenter.onCommentClicked(StoryItem("", "", commentsUrl, "", 0, 0, 0))

        // then:
        verify(view).startBrowser(commentsUrl)
    }

    private fun randomStoryItem(): StoryItem {
        return StoryItem("title", "url", "commentsUrl", "user", 1, 1, Random().nextLong())
    }
}