package io.lundgren.hnreader.presentation.di

import dagger.Component
import io.lundgren.hnreader.presentation.mvp.list.StoryListPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun createStoryListPresenter(): StoryListPresenter

}