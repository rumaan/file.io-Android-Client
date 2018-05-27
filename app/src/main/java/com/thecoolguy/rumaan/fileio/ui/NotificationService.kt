package com.thecoolguy.rumaan.fileio.ui

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.listeners.UploadListener
import com.thecoolguy.rumaan.fileio.repository.DisposableBucket
import com.thecoolguy.rumaan.fileio.repository.Repository

class NotificationService : Service(), UploadListener {
    override fun onComplete(fileEntity: FileEntity) {

    }

    override fun onUpload(fileEntity: FileEntity) {
        postNotification(fileEntity)

        // clear disposables
        DisposableBucket.clearDisposableBucket()
    }

    override fun progress(progress: Int) {
    }

    private val mBinder = LocalBinder()

    private fun postNotification(fileEntity: FileEntity) {
        NotificationHelper().create(applicationContext, fileEntity)
    }

    override fun onBind(intent: Intent): IBinder {
        // Repository..addUploadProgressListener(this)
        Repository.addObserver(this)
        return mBinder
    }

    class LocalBinder : Binder() {
        fun getService() = this
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // test
        return Service.START_NOT_STICKY
    }
}