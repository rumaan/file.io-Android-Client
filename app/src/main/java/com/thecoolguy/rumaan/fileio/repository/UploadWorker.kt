package com.thecoolguy.rumaan.fileio.repository

import android.net.Uri
import android.util.Log
import androidx.work.Worker
import com.thecoolguy.rumaan.fileio.data.db.DatabaseHelper
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.network.Uploader
import com.thecoolguy.rumaan.fileio.ui.NotificationHelper
import com.thecoolguy.rumaan.fileio.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class UploadWorker : Worker() {

    companion object {
        const val KEY_URI = "file_uri"
        private val TAG = UploadWorker::class.simpleName
    }

    private fun save(fileEntity: FileEntity) {
        val disposable = DatabaseHelper.saveToDatabase(fileEntity, Repository.getDao())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            /* Post a notification after saving */
                            NotificationHelper().create(applicationContext, fileEntity)
                        },
                        onError = {
                            Log.e(TAG, it.localizedMessage, it)
                        }
                )
        DisposableBucket.add(disposable)
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
                                val fileEntity = Uploader.composeIntoFileEntity(it, localFile)
                                // schedule this file object to be saved into the database
                                fileEntity?.let {
                                    save(it)
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

    override fun onStopped() {
        super.onStopped()
        DisposableBucket.clearDisposableBucket()
    }
}