package io.lundgren.hnreader.domain.interactors

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.plusAssign

abstract class BaseUseCase<T> constructor(val workScheduler: Scheduler, val resultScheduler: Scheduler) {

    protected val onSuccessStub: (T) -> Unit = {}
    protected val onErrorStub: (Throwable) -> Unit = { RxJavaPlugins.onError(OnErrorNotImplementedException(it)) }

    val disposables = CompositeDisposable()

    fun dispose() {
        disposables.clear()
    }

    protected fun Single<T>.executeUseCase(
            onSuccess: (T) -> Unit = onSuccessStub,
            onError: (Throwable) -> Unit = onErrorStub) {

        disposables += this
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe(onSuccess, onError)
    }
}