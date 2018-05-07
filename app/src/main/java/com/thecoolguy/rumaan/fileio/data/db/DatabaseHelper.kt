package com.thecoolguy.rumaan.fileio.data.db

import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.repository.Repository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object DatabaseHelper {

    private val TAG = DatabaseHelper.javaClass.simpleName

    fun saveToDatabase(fileEntity: FileEntity, mUploadItemDao: UploadItemDao) {
        Flowable.fromCallable {
            mUploadItemDao.insert(fileEntity)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Repository.getInstance().getAllItems()
                }
    }

    fun getAllItems(mUploadItemDao: UploadItemDao): Flowable<MutableList<FileEntity>> {
        return mUploadItemDao.allUploads
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }



}