package com.shepeliev.livemvi.mvicore

import androidx.lifecycle.LiveData
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

fun <T> ObservableSource<T>.toLivaData(): LiveData<T> = ObservableSourceLiveData(this)

private class ObservableSourceLiveData<T>(private val observableSource: ObservableSource<T>) :
    LiveData<T>() {

    private var disposable: Disposable? = null

    override fun onActive() {
        observableSource.subscribe(object : Observer<T> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: T) {
                postValue(t)
            }

            override fun onError(e: Throwable) {
                throw UnsupportedOperationException(
                    "ObservableSourceLiveData doesn't handle errors. " +
                            "You may want to map the error into the some state.", e
                )
            }
        })
    }

    override fun onInactive() {
        disposable?.dispose()
        disposable = null
    }
}
