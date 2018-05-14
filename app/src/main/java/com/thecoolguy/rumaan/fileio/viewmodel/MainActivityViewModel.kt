package com.thecoolguy.rumaan.fileio.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import android.util.Log
import com.thecoolguy.rumaan.fileio.data.LocalFile
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener
import com.thecoolguy.rumaan.fileio.listeners.FileUploadProgressListener
import com.thecoolguy.rumaan.fileio.repository.Repository
import com.thecoolguy.rumaan.fileio.utils.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Abstraction for Repository with Lifecycle aware stuffs
 */

class MainActivityViewModel : ViewModel() {
    /* Store the current chosen file instance */
    private lateinit var localFile: LocalFile

    /**
     * Get the file from the database and save in the current view model state.
     */
    fun chooseFileFromUri(context: Context, fileUri: Uri) {
        val fileLoadListener = context as FileLoadListener
        val fileObservable = getLocalFileObservable(context, fileUri)
        fileObservable.subscribeBy(
                onSuccess = {
                    localFile = it
                    fileLoadListener.onFileLoad(it)
                },
                onError = {
                    fileObservable.retry(1)
                    Log.e(TAG, it.localizedMessage, it)
                }
        )
    }

    private fun getLocalFileObservable(context: Context, fileUri: Uri): Single<LocalFile> =
            Single.fromCallable { (Utils.getLocalFile(context, fileUri)) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())


    /**
     * Upload the file to server and save the response into the database.
     *
     * @param listener Callback class where the upload callbacks must be published.
     * */
    fun uploadFile(listener: FileUploadProgressListener) {
        // upload file
        Repository.getInstance().upload(localFile, listener)

        // TODO: Show progress window here.
    }

    companion object {
        private val TAG = "MainActivityViewModel"
    }

}
