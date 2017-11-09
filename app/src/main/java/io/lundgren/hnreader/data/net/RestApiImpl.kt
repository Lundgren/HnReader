package io.lundgren.hnreader.data.net

import io.lundgren.hnreader.data.net.models.ApiStory
import io.lundgren.hnreader.domain.models.Story
import io.reactivex.Observable
import javax.inject.Inject

class RestApiImpl @Inject constructor(private val hnService: HackerNewsService) : RestApi {

    override fun getFrontPageStories(): Observable<List<Story>> {
        return hnService.getFrontPageStories()
                .map { it.map(this::apiToStory) }
    }

    private fun apiToStory(apiStory: ApiStory): Story {
        return Story(
                apiStory.id,
                apiStory.title,
                fixUrl(apiStory.url),
                apiStory.user ?: "",
                apiStory.points,
                apiStory.comments_count,
                apiStory.time * 1000
        )
    }

    private fun fixUrl(url: String): String {
        return when {
            url.startsWith("item?id=") -> "https://news.ycombinator.com/$url"
            url.startsWith("http") -> url
            else -> "http://$url"

        }
    }
}