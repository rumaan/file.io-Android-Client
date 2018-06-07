package com.thecoolguy.rumaan.fileio.repository

import android.app.Application
import com.thecoolguy.rumaan.fileio.data.db.DatabaseHelper
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.listeners.DatabaseCallback
import com.thecoolguy.rumaan.fileio.listeners.UploadListener
import com.thecoolguy.rumaan.fileio.network.Uploader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object Repository : UploadListener, DatabaseCallback {
    override fun onComplete(fileEntity: FileEntity) {
    }

    override fun onSave(fileEntity: FileEntity, id: Long) {
        // on file entity saved to database callback
        observerList.forEach {
            it?.onComplete(fileEntity)
        }
    }

    // use this as context throughout
    lateinit var applicationContext: Application

    private val mDao: UploadItemDao by lazy {
        UploadHistoryRoomDatabase.getInstance(applicationContext)
                .uploadItemDao()
    }

    fun getDao() = mDao

    private val observerList = mutableListOf<UploadListener?>()

    fun addObserver(uploadListener: UploadListener) {
        if (!observerList.contains(uploadListener)) {
            observerList.add(uploadListener)
        }
    }

    private fun saveToDb(fileEntity: FileEntity) {
        val disposable = DatabaseHelper.saveToDatabase(fileEntity, mDao)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    /* Notify all listeners */
                    observerList.forEach {
                        it?.onComplete(fileEntity)
                    }
                })
        DisposableBucket.add(disposable)
    }

    fun upload(localFile: LocalFile, uploadListener: UploadListener) {
        // add the listener to list
        addObserver(uploadListener)

        // this refers to DatabaseCallback
        Uploader.upload(localFile, this)
    }

    override fun onUpload(fileEntity: FileEntity) {
        saveToDb(fileEntity)
    }

    override fun progress(progress: Int) {
    }

}