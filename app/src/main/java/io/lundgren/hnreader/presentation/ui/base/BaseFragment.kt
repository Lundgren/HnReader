package io.lundgren.hnreader.presentation.ui.base

import android.support.v4.app.Fragment
import io.lundgren.hnreader.presentation.mvp.base.BasePresenter

abstract class BaseFragment<out P : BasePresenter<V>, in V> : Fragment() {

    abstract val presenterKey: String
    abstract fun createPresenter(): P

    override fun onStart() {
        super.onStart()

        getPresenter().onViewAttached(this as V)
    }

    override fun onStop() {
        super.onStop()

        getPresenter().onViewDetach()
    }

    fun getPresenter(): P {
        return (activity as PresenterCache).getPresenter(presenterKey, this::createPresenter)
    }
}
