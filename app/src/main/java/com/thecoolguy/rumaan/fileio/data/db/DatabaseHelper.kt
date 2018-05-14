package com.thecoolguy.rumaan.fileio.data.db

import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.listeners.DatabaseCallback
import com.thecoolguy.rumaan.fileio.repository.DisposableBucket
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object DatabaseHelper {

    private val TAG = DatabaseHelper.javaClass.simpleName

    fun saveToDatabase(fileEntity: FileEntity, mUploadItemDao: UploadItemDao,
                       databaseCallback: DatabaseCallback) {
        val flowable = Flowable.fromCallable {
            mUploadItemDao.insert(fileEntity)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val disposable = flowable.subscribeBy {
            databaseCallback.onSave(fileEntity, it)
            // Repository.getInstance().getAllItems()
        }

        DisposableBucket.add(disposable)
    }

    fun getAllItems(mUploadItemDao: UploadItemDao): Flowable<MutableList<FileEntity>> {
        return mUploadItemDao.allUploads
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}