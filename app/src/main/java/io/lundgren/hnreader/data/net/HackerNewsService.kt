package io.lundgren.hnreader.data.net

import io.lundgren.hnreader.data.net.models.ApiStory
import io.reactivex.Observable
import retrofit2.http.GET

interface HackerNewsService {

    @GET("news")
    fun getFrontPageStories(): Observable<List<ApiStory>>

}