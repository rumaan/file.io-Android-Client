package com.thecoolguy.rumaan.fileio.data.db

import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object DatabaseHelper {

    private val TAG = DatabaseHelper.javaClass.simpleName

    fun saveToDatabase(fileEntity: FileEntity, mUploadItemDao: UploadItemDao) = Single.fromCallable {
        mUploadItemDao.insert(fileEntity)
    }

    fun getAllItems(mUploadItemDao: UploadItemDao): Flowable<MutableList<FileEntity>> {
        return mUploadItemDao.allUploads
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}