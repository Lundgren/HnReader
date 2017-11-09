package io.lundgren.hnreader.data.net

import io.lundgren.hnreader.domain.models.Story
import io.reactivex.Observable

interface RestApi {
    fun getFrontPageStories(): Observable<List<Story>>
}