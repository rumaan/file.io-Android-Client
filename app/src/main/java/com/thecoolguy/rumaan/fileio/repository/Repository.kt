package com.thecoolguy.rumaan.fileio.repository

import android.app.Application
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.data.db.DatabaseHelper
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.listeners.DatabaseCallback
import com.thecoolguy.rumaan.fileio.listeners.UploadListener
import com.thecoolguy.rumaan.fileio.network.Uploader

object Repository : UploadListener, DatabaseCallback {
    override fun onSave(fileEntity: FileEntity, id: Long) {
        // on file entity saved to database callback
    }

    // use this as context throughout
    lateinit var applicationContext: Application

    private val mDao: UploadItemDao by lazy {
        UploadHistoryRoomDatabase.getInstance(applicationContext)
                .uploadItemDao()
    }

    private val observerList = mutableListOf<UploadListener?>()

    fun addObserver(uploadListener: UploadListener) {
        if (!observerList.contains(uploadListener)) {
            observerList.add(uploadListener)
        }
    }

    private fun saveToDb(fileEntity: FileEntity) {
        DatabaseHelper.saveToDatabase(fileEntity,
                mDao, this)
    }

    fun upload(localFile: LocalFile, uploadListener: UploadListener) {
        // add the listener to list
        addObserver(uploadListener)
        // TODO: change this
        Uploader.upload(localFile, this)
    }

    override fun onComplete(fileEntity: FileEntity) {
        // notify all observers
        observerList
                .forEach {
                    it?.onComplete(fileEntity)
                }

        // callback for file upload success
        // TODO: uncomment this
        // saveToDb(fileEntity)
    }

    override fun progress(progress: Int) {
    }


}