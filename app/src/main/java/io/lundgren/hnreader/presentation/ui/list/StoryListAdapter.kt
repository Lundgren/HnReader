package io.lundgren.hnreader.presentation.ui.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.lundgren.hnreader.R
import io.lundgren.hnreader.domain.interactors.models.StoryItem
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class NewsViewHolder constructor(private val view: View) : RecyclerView.ViewHolder(view) {

    private val prettyTime = PrettyTime()
    private val titleView = view.findViewById<TextView>(R.id.title)
    private val usernameView = view.findViewById<TextView>(R.id.username)
    private val timeView = view.findViewById<TextView>(R.id.time)
    private val urlView = view.findViewById<TextView>(R.id.url)
    private val comments = view.findViewById<TextView>(R.id.comments)
    private val score = view.findViewById<TextView>(R.id.score)

    fun bind(story: StoryItem, storyListener: (StoryItem) -> Unit, commentsListener: (StoryItem) -> Unit) {
        titleView.text = story.title
        usernameView.text = story.user
        timeView.text = prettyTime.format(Date(story.time))
        urlView.text = story.url
        score.text = "+${story.score}"
        comments.text = story.commentsCount.toString()

        view.setOnClickListener { storyListener(story) }
        comments.setOnClickListener { commentsListener(story) }
    }
}

class StoryListAdapter constructor(
        private var stories: List<StoryItem>,
        private val storyListener: (StoryItem) -> Unit,
        private val commentsListener: (StoryItem) -> Unit
) : RecyclerView.Adapter<NewsViewHolder>() {

    override fun getItemCount() = stories.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(stories[position], storyListener, commentsListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_story, parent, false)
        return NewsViewHolder(view)
    }

    fun setStories(stories: List<StoryItem>) {
        this.stories = stories
        notifyDataSetChanged()
    }

}