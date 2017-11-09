package io.lundgren.hnreader.presentation.ui.base

import io.lundgren.hnreader.presentation.mvp.base.BasePresenter

interface PresenterCache {
    fun <P : BasePresenter<V>, V> getPresenter(key: String, factory: () -> P): P
}