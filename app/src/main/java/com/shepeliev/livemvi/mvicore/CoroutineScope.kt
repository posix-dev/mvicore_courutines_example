package com.shepeliev.livemvi.mvicore

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <T> CoroutineScope.rxFlow(block: suspend ObservableEmitter<T>.() -> Unit): Observable<T> {
    return Observable.create {
        launch { block(it) }
    }
}
