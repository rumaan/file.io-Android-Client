package com.thecoolguy.rumaan.fileio.repository

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

object DisposableBucket {

    private val compositeDisposable = CompositeDisposable()

    fun size() = compositeDisposable.size()

    fun clearDisposableBucket() {
        compositeDisposable.clear()
    }

    fun add(disposable: Disposable) {
        compositeDisposable.addAll(disposable)
    }
}