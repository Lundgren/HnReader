package io.lundgren.hnreader.presentation.mvp.list

import io.lundgren.hnreader.domain.interactors.GetFrontPageStoriesUseCase
import io.lundgren.hnreader.domain.interactors.models.StoryItem
import io.lundgren.hnreader.presentation.mvp.base.BasePresenter
import javax.inject.Inject

class StoryListPresenter @Inject constructor(val getFrontPageStories: GetFrontPageStoriesUseCase) : BasePresenter<StoryListView> {

    var view: StoryListView? = null

    override fun onViewAttached(view: StoryListView) {
        this.view = view

        this.view?.displayLoading()
        getFrontPageStories.execute(this::onFrontPageData, this::onFrontPageError)
    }

    override fun onViewDetach() {
        getFrontPageStories.dispose()
        view = null
    }

    fun onForceFetch() {
        view!!.displayLoading()
        getFrontPageStories.execute(this::onFrontPageData, this::onFrontPageError, true)
    }

    fun onStoryClicked(story: StoryItem) {
        view!!.startBrowser(story.url)
    }

    fun onCommentClicked(story: StoryItem) {
        view!!.startBrowser(story.commentsUrl)
    }

    private fun onFrontPageData(stories: List<StoryItem>) {
        view!!.hideLoading()
        view!!.displayStories(stories)
    }

    private fun onFrontPageError(ex: Throwable) {
        view!!.hideLoading()
        view!!.displayError()
    }
}