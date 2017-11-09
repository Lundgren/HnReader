package io.lundgren.hnreader.presentation.ui.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.lundgren.hnreader.R
import io.lundgren.hnreader.domain.interactors.models.StoryItem
import io.lundgren.hnreader.presentation.App
import io.lundgren.hnreader.presentation.mvp.list.StoryListPresenter
import io.lundgren.hnreader.presentation.mvp.list.StoryListView
import io.lundgren.hnreader.presentation.ui.base.BaseFragment

class StoryListFragment : BaseFragment<StoryListPresenter, StoryListView>(), StoryListView {

    override val presenterKey = "storyPresenter"
    override fun createPresenter() = App.appComponent.createStoryListPresenter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var refresh: SwipeRefreshLayout
    private val adapter = StoryListAdapter(emptyList(), this::onStoryClicked, this::onCommentClicked)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_story_list, container, false)

        refresh = v.findViewById(R.id.swipeRefresh)
        refresh.setOnRefreshListener { getPresenter().onForceFetch() }

        recyclerView = v.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)

        return v
    }

    override fun displayStories(stories: List<StoryItem>) {
        adapter.setStories(stories)
    }

    override fun displayLoading() {
        refresh.isRefreshing = true
    }

    override fun hideLoading() {
        refresh.isRefreshing = false
    }

    override fun displayError() {
        Toast.makeText(context, R.string.fetch_error_msg, Toast.LENGTH_LONG).show()
    }

    override fun startBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun onStoryClicked(story: StoryItem) {
        getPresenter().onStoryClicked(story)
    }

    fun onCommentClicked(story: StoryItem) {
        getPresenter().onCommentClicked(story)
    }
}