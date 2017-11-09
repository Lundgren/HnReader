package io.lundgren.hnreader.domain.interactors

import io.lundgren.hnreader.domain.ResultScheduler
import io.lundgren.hnreader.domain.WorkScheduler
import io.lundgren.hnreader.domain.interactors.models.StoryItem
import io.lundgren.hnreader.domain.models.Story
import io.lundgren.hnreader.domain.repositories.StoryRepository
import io.reactivex.Scheduler
import javax.inject.Inject

class GetFrontPageStoriesUseCase @Inject constructor(
        val storyRepository: StoryRepository,
        @WorkScheduler workScheduler: Scheduler,
        @ResultScheduler resultScheduler: Scheduler
) : BaseUseCase<List<StoryItem>>(workScheduler, resultScheduler) {

    fun execute(onSuccess: (List<StoryItem>) -> Unit, onError: (Throwable) -> Unit = onErrorStub, forceFresh: Boolean = false) {
        storyRepository
                .getFrontPageStories(forceFresh)
                .map { storiesToItems(it) }
                .executeUseCase(onSuccess, onError)
    }

    private fun storiesToItems(stories: List<Story>): List<StoryItem> {
        return stories.map {
            StoryItem(
                    it.title,
                    it.url,
                    "https://news.ycombinator.com/item?id=${it.id}",
                    it.user,
                    it.score,
                    it.commentsCount,
                    it.time
            )
        }
    }
}