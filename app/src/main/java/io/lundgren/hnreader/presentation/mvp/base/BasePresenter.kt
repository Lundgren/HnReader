package io.lundgren.hnreader.presentation.mvp.base

interface BasePresenter<in V> {
    fun onViewAttached(view: V)
    fun onViewDetach()
}