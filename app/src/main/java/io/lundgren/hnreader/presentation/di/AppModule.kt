package io.lundgren.hnreader.presentation.di

import dagger.Module
import dagger.Provides
import io.lundgren.hnreader.data.cache.InMemStoryCache
import io.lundgren.hnreader.data.cache.StoryCache
import io.lundgren.hnreader.data.net.HackerNewsService
import io.lundgren.hnreader.data.net.RestApi
import io.lundgren.hnreader.data.net.RestApiImpl
import io.lundgren.hnreader.data.repositories.CachedStoryRepository
import io.lundgren.hnreader.domain.ResultScheduler
import io.lundgren.hnreader.domain.WorkScheduler
import io.lundgren.hnreader.domain.repositories.StoryRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @WorkScheduler
    fun providesWorkScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Singleton
    @ResultScheduler
    fun providesResultScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    fun providesStoryCache(): StoryCache {
        return InMemStoryCache(5 * 60 * 60 * 1000)
    }

    @Provides
    @Singleton
    fun providesRestApi(hnService: HackerNewsService): RestApi {
        return RestApiImpl(hnService)
    }

    @Provides
    @Singleton
    fun providesStoryRepository(restApi: RestApi, storyCache: StoryCache): StoryRepository {
        return CachedStoryRepository(restApi, storyCache)
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("http://node-hnapi.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun providesHackerNewsService(retrofit: Retrofit): HackerNewsService {
        return retrofit.create(HackerNewsService::class.java)
    }
}