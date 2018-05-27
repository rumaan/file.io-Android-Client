package com.thecoolguy.rumaan.fileio.repository

import android.net.Uri
import android.util.Log
import androidx.work.Worker
import com.thecoolguy.rumaan.fileio.network.Uploader
import com.thecoolguy.rumaan.fileio.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class UploadWorker : Worker() {


    companion object {
        const val KEY_URI = "file_uri"
        private val TAG = UploadWorker::class.simpleName
    }

    override fun doWork(): WorkerResult {
        val fileUri = inputData.getString(KEY_URI, null)
        fileUri?.let { it ->
            // get the local file object from the backing storage
            val localFile = Utils.getLocalFile(applicationContext, Uri.parse(it))
            val uploaderObservable = Uploader
                    .getUploadObservable(localFile)

            val disposable = uploaderObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onSuccess = {
                                val fileEntity = Uploader.getFileEntity(it, localFile)
                                // schedule this file object to be saved into the database
                                fileEntity?.let {
                                    Log.d(TAG, it.toString())
                                    // Repository.getInstance().onFileUpload(fileEntity)
                                    Repository.onComplete(it)
                                }
                            },
                            onError = {
                                uploaderObservable.retry(2)
                                Log.e(TAG, "Error Uploading the file: ${it.localizedMessage}", it)
                            }
                    )

            DisposableBucket.add(disposable)

            return WorkerResult.SUCCESS
        }

        return WorkerResult.FAILURE
    }
}